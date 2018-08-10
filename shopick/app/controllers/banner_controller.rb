class BannerController < ApplicationController

	def get_banner
	  postcol = Banner.where(visible: true)
	  render :json => postcol.order("created_at desc"), each_serializer: BannerSerializer, root: false
	end

	def get_all_offers
	  postcol = Banner.where(visibleAsOffer: true)
	  render :json => postcol.order("top DESC, created_at desc"), each_serializer: BannerSerializer, root: false
	end


	def like_banner
		##getting current user for likes
	  c_user_email = params[:user_email].presence
	  c_user = User.find_by_email(c_user_email)
	  banner = Banner.where(globalID: params[:globalID]).first
	  user_email = params[:user_email].presence
	  user = User.find_by_email(user_email)
	  if  not user.banners.exists?(banner)
	  	user.banners << banner 
	  	AffinityScore.process_banner_like(user, banner)
	  end
	  render :json => banner, root: false, c_user: c_user
      User.send_admin_users_notification(" user   : "+ user.email + " liked Banner with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/banner?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	  
	end

	def unlike_banner
		##getting current user for likes
	    c_user_email = params[:user_email].presence
	    c_user = User.find_by_email(c_user_email)

	  	banner = Banner.where(globalID: params[:globalID]).first
	    user_email = params[:user_email].presence
	    user = User.find_by_email(user_email)
	    if user.banners.exists?(banner)
	      user.banners.delete(banner)
	      AffinityScore.process_banner_unlike(user, banner)
	    end
	    render :json => banner, root: false, c_user: c_user
      	User.send_admin_users_notification(" user   : "+ user.email + " unliked Banner with globalID : "+ params[:globalID],
        "http://shopick.co.in/admin/banners?utf8=%E2%9C%93&q%5BglobalID_contains%5D="+params[:globalID]+"&commit=Filter&order=id_desc")
	 
	end

  def find_banner
    @findthis =  FindThi.new(findthis_params)
    @findthis.save
    value  = params[:user_id]
    if value.eql?  "-1"
      value = params[:temp_user_id]
    end
    
    if value.eql? "-1"
    else
      queryuser = User.find(value)
      queryuser.update(:phoneno => params[:phoneno])
      banner = Banner.where(globalID: params[:globalID]).first
      AffinityScore.process_banner_like(queryuser, banner)      
      FirstNotificationJob.perform_in(10.seconds, 20, queryuser.id)
    end
    User.send_admin_users_findthis_notification(@findthis)
    render :json => @findthis
  end

  def findthis_params
      params.permit(:globalID, :user_id, :banner_id, :lat, :lon, :phoneno)
  end

end
