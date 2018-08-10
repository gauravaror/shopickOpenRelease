class TipController < ApplicationController
	def index
		@tip =  Tip.all
		render :json =>  { "presentations" => @tip.reverse.as_json(:root => false)}.to_json
	end

	def presentation_desc
		 ##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

		@presentation =  Tip.find(params[:presentation_id])
		render :json =>  @presentation, each_serializer: TipSerializer, root: false, c_user: c_user
	end

	def presentation_desc_global
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

		@presentation =  Tip.find_by(:globalID => params[:presentation_id])
		render :json =>  @presentation, each_serializer: TipSerializer, root: false, c_user: c_user
	end

	def presentations
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

		if params[:filter]
			@tip =  Tip.joins(:tip_categorizations).where('tip_categorizations.category_id' => params[:filter])
		else
			@tip =  Tip.all
		end
		render :json => @tip.reverse, each_serializer: TipSerializer, root: false, c_user: c_user
	end

	def like_tip
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

	  tip = Tip.where(globalID: params[:globalID]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if  not user.tips.exists?(tip)
	  	user.tips << tip
	  	AffinityScore.process_tips_like(user, tip)
	  end
	  render :json => tip, each_serializer: TipSerializer, root: false, c_user: c_user
      	User.send_admin_users_notification(" user   : "+ user.email + " liked tip with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/tips?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	end

	def unlike_tip
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

	  	tip = Tip.where(globalID: params[:globalID]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.tips.exists?(tip)
	      user.tips.delete(tip)
	      AffinityScore.process_tips_unlike(user, tip)
	    end
	    render :json => tip, each_serializer: TipSerializer, root: false, c_user: c_user
      	User.send_admin_users_notification(" user   : "+ user.email + " unliked tip with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/tips?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	end
end
