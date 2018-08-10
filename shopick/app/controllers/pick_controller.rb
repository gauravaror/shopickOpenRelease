class PickController < ApplicationController
	def earn_pick
		earn = EarnPick.where(active: true)
		render :json => earn, root: false
	end

	def redeem_pick
		earn = RedeemPick.where(active: true)
		render :json => earn, root: false
	end

	def user_redeem_pick
		value  = params[:user_id]
	    if value.eql?  "-1"
	      value = params[:temp_user_id]
	      params[:user_id] = params[:temp_user_id]
	    end
	    @user_redeem_pick =  UserRedeemPick.new(user_redeem_pick_params)
	    @user_redeem_pick.save
	    if value.eql? "-1"
	    else
	      queryuser = User.find(value)
	      queryuser.update(:phoneno => params[:phoneno])
	    end
	    User.send_admin_users_notification(" User asked to reedem picks usercode   " + value.to_s, " User asked to reedem picks usercode   " + value.to_s,
    		"")
	    render :json => @user_redeem_pick
	end

	def user_redeem_pick_params
	      params.permit(:globalID, :user_id, :redeem_pick_id, :phoneno)
	end

	def get_current_monthly_campign
		 render :json =>  { 
		 	"rules" => "Monthly 3 Leading users on Leaderboard win 1000 Rs to spend on brands they like.",
		 	"imageUrl" => "" }.to_json
	end

	def reject_picks_history
		picks_history =  PicksHistory.find(params[:id])
		json_response = PicksHistory.reject_pick_history(picks_history)
		render :json => json_response
	end

	def approve_picks_history
		picks_history =  PicksHistory.find(params[:id])
		json_response  = PicksHistory.approve_pick_history(picks_history)
		render :json => json_response
	end


end