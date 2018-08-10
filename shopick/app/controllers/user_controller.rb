class UserController < ApplicationController
	skip_filter :authenticate_user!, :only => :create_auth


	def create_auth
		email = params.permit(:email)
		user = User.find_by(:email => params[:email])
		if user && (not params[:email].blank?)
			user.update(user_params)
			user.save
		else 
			user = User.find_by(:instanceID => params[:instanceID])	
			if user && (not params[:instanceID].blank?)
				user.update(user_params)
				user.save
			else
				user = User.find_by(:uniq_device_id => params[:uniq_device_id])	
				if user && (not params[:uniq_device_id].blank?)
					user.update(user_params)
					user.save					
				else
					process_code
					user = User.create(user_params)
					user.save
				end
			end
		end
		if user.signup
			# Do Nothing
		  User.send_admin_users_notification(" again signing up on shopick added picks  : "+ user.email + " user id "+ user.id.to_s,
        "http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+user.email+"&commit=Filter&order=id_desc")
		else
			user.signup = true
			user.save
			User.update_picks_histories(user.id)
		end
		
		render :json => user, each_serializer: ActualUserSerializer, root: false
	end

  def user_params
    params.permit( :email, :instanceID, :gcm_token, :name , :uniq_device_id,:picks, :loginType, :service_id, :service_token, :profileImage, :usercode, :coverImage, :monthlyPicks,  :gender, :age_max, :age_min , :password, :password_confirmation, :test_user, :fake_user)
  end

  def send_email
  	@user = User.find_by(:email => "hiremath.in@gmail.com")
  	UserMailer.welcome_email(@user).deliver_now
  end

def update_token
		token_params
		user = User.find_by(:email => params[:email])
		if user && (not params[:email].blank?)
			user.update(gcm_token: params[:gcm_token])
			user.update(instanceID: params[:instanceID])
			user.save
		else 
			user = User.find_by(:instanceID => params[:instanceID])	
			if user && (not params[:instanceID].blank?)
				user.update(gcm_token: params[:gcm_token])
				user.save
			else

				user = User.find_by(:uniq_device_id => params[:uniq_device_id])	
				if user && (not params[:uniq_device_id].blank?)
					user.update(gcm_token: params[:gcm_token])
					user.update(instanceID: params[:instanceID])
					user.save
				else
					process_password
					user = User.create(token_params)
					user.save
					UserMailer.welcome_email(user.id).deliver_later!(wait: 1.hour)
					FirstNotificationJob.perform_in(10.minutes, 12, user.id)
					FirstNotificationJob.perform_in(10.seconds, 21, user.id)
				end
			end
		end
		User.send_admin_users_notification(" Someone downloaded the shopick app  : "+ user.email+ " user id "+ user.id.to_s,
        "http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+user.email+"&commit=Filter&order=id_desc")
		render :json => user, each_serializer: ActualUserSerializer, root: false
end
  
  def token_params
  	params.permit(:email, :gcm_token, :instanceID, :uniq_device_id ,:picks, :loginType, :service_id, :service_token, :monthlyPicks, :referred, :usercode,:password, :password_confirmation)
  end

  def process_password
  	if params[:email].blank?
  		params[:email] = "Shop_"+SecureRandom.base64(3).delete('/+=')[0, 3]+"@shopick.co.in"	
  	end
  	puts params[:email] + " email "
  	params[:usercode] = get_usercode(params[:email])
  	params[:referred] = false
  	params[:picks] = 10
  	params[:monthlyPicks] = 10
  	params[:password] = "fjkhj@lkj"
  	params[:password_confirmation] = "fjkhj@lkj"	
  end

  def process_code
  	if params[:usercode].blank?
  		params[:usercode] = get_usercode(params[:email])
  	end
  	 if params[:picks].blank?
  		params[:picks] = 10
  	end
  	if params[:monthlyPicks].blank?
  		params[:monthlyPicks] = 10
  	end
  	if params[:referred].blank?
  		params[:referred] = false
  	end
  end


  def my_referral_code
	user = User.find_by(:email => params[:default_email])
	if user
		 render :json =>  { "referralCode" => user.usercode,
		 	"rules" => "Get 50 Picks when your first friend installs the app. After that every successful referral will fetch you 25 picks.\n\n You would need 100 picks to redeem. Redemption of 100 picks will get you Rs. 100 either in your digital wallet or your bank account! ",
		 	"imageUrl" => "referandwin.jpg" }.to_json
	end
  end

def redeem_referral_service
	
	referred_user_ = User.find_by(:instanceID => params[:instanceID])
	if referred_user_.referred
		render :json =>  { "statusCode" => 400,
		 						"message" => "Your have already redeemed your referral!"}.to_json
	else
		#process_usercode
		cod = params[:usercode]
		nod = cod.chomp('"').reverse.chomp('"').reverse
		referrer_user_ = User.find_by(:usercode => nod)
		if referrer_user_
			if referrer_user_.usercode.eql? referred_user_.usercode
				render :json =>  { "statusCode" => 400,
			 		"message" => "You can't refer yourself!"}.to_json

			else
				referred_user_.referred =  true
				referred_user_.referred_user.create(user_id: referred_user_.id, referred_user_id: referrer_user_.id, 
							referred_usercode: referred_user_.usercode, referrer_usercode: referrer_user_.usercode)

				referred_user_.picks_history.create(pickTransaction: 50,
					user_id: referrer_user_.id,
					pickType: 0,
					globalID:  SecureRandom.uuid,
					processed: false,
					notificationMessage: "Picks for acception referral request",
				 transactionReason: "Add the referral bonus redumption of invite for new user with usercode : "+ referrer_user_.usercode)

				referrer_user_.picks_history.create(pickTransaction: 50,
					user_id: referred_user_.id,
					pickType: 0,
					globalID:  SecureRandom.uuid,
					processed: false,
					notificationMessage: "Picks for successful referral request",
					transactionReason: "Add the referral bonus for referring new user new user with usercode : "+ referred_user_.usercode)

				referred_user_.picks_history.create(pickTransaction: 50,
					user_id: referrer_user_.id,
					pickType: 1,
					processed: false,
					globalID:  SecureRandom.uuid,
					notificationMessage: "Picks for accepting referral request",
				 transactionReason: "Add monthlyPicks the referral bonus redumption of invite for new user with usercode : "+ referrer_user_.usercode)


				referrer_user_.picks_history.create(pickTransaction: 50, 
					user_id: referred_user_.id,
					pickType: 1,
					globalID:  SecureRandom.uuid,
					processed: false,
					notificationMessage: "Picks for successful referral request",
					transactionReason: "Add monthlyPicks the referral bonus for referring new user new user with usercode  : "+ referred_user_.usercode)

				referred_user_.save
				referrer_user_.save
				User.send_admin_users_notification("Refferal done successfully refferrer  " + referrer_user_.usercode + " refferred user " + referred_user_.usercode,
        			"Refferred User :  http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+referred_user_.email+"&commit=Filter&order=id_desc 
        			   Refferrer User :  http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+referrer_user_.email+"&commit=Filter&order=id_desc")
				render :json =>  { "statusCode" => 200,
			 			"message" => "Thanks, your reward will be credited shortly!"}.to_json
		 	end
		else 
					render :json =>  { "statusCode" => 400,
		 						"message" => "Invalid Referral Code!"}.to_json

		end
	end

end

def get_leaderboard
	current_user = User.find_by(:email => params[:default_email])
	users = User.all.order("monthlyPicks DESC")
	render :json => users, each_serializer: LeaderboardSerializer , root: false
end


def get_my_picks
	current_user = User.find_by(:email => params[:default_email])
	render :json => current_user, each_serializer: ActualUserSerializer, root: false
end

###Helper Function
def get_usercode(email)
  	splitscreen = email.split("@")
  	size =  4
  	value = SecureRandom.base64(size).delete('/+=')[0, size]
  	finalcode = splitscreen[0]+"_"+value
  	return finalcode
  end

end
