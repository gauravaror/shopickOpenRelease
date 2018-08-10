class NotifyController < ApplicationController
	before_filter :authenticate_user_from_token!, :except => [:index, :privacy, :findanything, :findpath, :completedfindanything, :redeem_referral]
	before_filter :authenticate_user!, :except => [:index, :privacy, :findanything, :findpath, :completedfindanything, :redeem_referral]

	def privacy
	end

	def index

		@post = Post.where("top = ?", true).limit(1)
		set_meta_tags title: 'Home',
              description: 'Shopick is your true personal shopping assistant.
. Discover what people are buying around you.
. Your personal Assistant to navigate through tons of products. Follow current trends and products through curated editorials.
Want to buy a jeans. Before moving out to store, Check latest collections of all the brands like Levis, Wrangler.  Get discounts at selected merchants. Share your purchases with fellow shoppers!! Flaunt them. You will never have to bet or make a blind decision at offline store.
 People have posted exciting offers from brands like van heusen, Levis, Reebok, Peter England, Lacoste, Nike, Allen Solly, Tommy Hilfiger, Levis, Park Avenue, Incense, VDOT by van heusen, Madame, COBB, BATA, HIDESIGN, Marks and Spencer, Pepe jeans, Wills Lifestyle, designer wear in Delhi, etc

.',
              keywords: 'Offer, Sales, Discount, Jeans , Shirt, Tshirt, Shoes, Latest collection ,van heusen, Levis, Reebok, Peter England, Lacoste, Nike, Allen Solly, Tommy Hilfiger, Levis, Park Avenue, Incense, HIDESIGN, Marks and Spencer, Pepe jeans, Forever new, Sarojni Haul',
              icon: ActionController::Base.helpers.asset_path("logo.png"),
              publisher: "https://play.google.com/store/apps/details?id=com.acquire.shopick"

	end


	def search
		render :json => Elasticsearch::Model.search(params[:query], [Brand, Category,Post, Product]).results.to_json
	end


	def search_location
		render :json => Elasticsearch::Model.search(params[:query], [Brand, Store, Location]).results.to_json
	end

	def search_type
		render :json => Elasticsearch::Model.search(params[:query], [Category]).results.to_json
	end

	def search_postCollection
		render :json => Elasticsearch::Model.search(params[:query], [PostCollection]).results.to_json
	end

	def findanything
		@findanything = Findanything.new

	end

	def findpath
		@findanything = Findanything.new
		if params[:findanything]
			addGloabalID
			@findanything =  Findanything.new(findpath_params)
			@findanything.save
			user = User.find_by(:email => "gauravarora.daiict@gmail.com")
			user2 = User.find_by(:email => "hiremath.in@gmail.com")
			if user
				FindanythingMailer.findanything_email(@findanything, user).deliver_later
			end
			if user2
				FindanythingMailer.findanything_email(@findanything, user2).deliver_later
			end
		end
	end

	def completedfindanything
		@findanything = Findanything.new
		if  params[:id]
			@findanything = Findanything.find_by(:globalID => params[:id])
		else
			@findanything = Findanything.find_by(:globalID => params[:findanything][:globalID])
			@findanything.update(findpath_params);
			@findanything.save
		end
	end

	def addGloabalID
		params[:findanything][:globalID] = SecureRandom.uuid;
		params[:findanything][:status] = "Our operations team is visiting stores near you, get the required information & resolve your query. Expect a call from Shopick soon!!";
	end

	def findpath_params
    	params.require(:findanything).permit(:name, :email, :phoneno, :description, :findanything_image, :globalID, :locality, :lat, :lon, :status)
  	end


	def gcm_notification
		puts params[:id];
		NotificationJob.perform_async(params[:id]);
	end

	def gcm_notification_all
		puts params[:id];
		NotifyAllJob.perform_async(params[:id]);
	end


	def gcm_notification_male
		puts params[:id];
		NotifyGenderJob.perform_async(params[:id], 0);
	end


	def gcm_notification_female
		puts params[:id];
		NotifyGenderJob.perform_async(params[:id], 1);
	end
	def redeem_referral
		redir_url = "https://play.google.com/store/apps/details?id=com.acquire.shopick&pcampaignid=onreferal"
		if params[:code]
			redir_url = "https://play.google.com/store/apps/details?id=com.acquire.shopick&pcampaignid=fdl_&referrer=deep_link_id%3Dandroid://www.shopick.co.in/redeem_referral/"+params[:code]
			puts "new did "+params[:code]
		end
		puts "Redirecting referral request to url "+ redir_url
		redirect_to redir_url
	end
end
