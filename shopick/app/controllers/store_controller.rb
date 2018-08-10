class StoreController < ApplicationController
	def stores
		lat = params["lat"]
		lon = params["lon"]
		distance =  params["distance"]
		if distance
		else 
			distance = 10
		end
		if lat && lon
			@store = Store.find_by_sql("SELECT *, ( 3959 * acos( cos( radians("+lat.to_s+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+lon.to_s+") ) + sin( radians("+lat+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores HAVING distance < " + distance.to_s + " ORDER BY distance  LIMIT  50 ;");
			#@store = Store.find_by_sql("SELECT *, ( 3959 * acos( cos( radians("+lat.to_s+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+lon.to_s+") ) + sin( radians("+lat+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores ORDER BY distance ;");
		else 
			@store =  Store.all.limit(50)
		end
		render :json => @store, root: false
	end

	def metaindex
    	@stores = Store.all
    	render :json =>  { "stores" => @stores.as_json(:root => false)}.to_json
  	end

	def closest_stores
		lat = params["lat"]
		lon = params["lon"]
		distance =  params["distance"]
		if distance
		else 
			distance = 10
		end
		@stores = Store.find_by_sql("SELECT *, ( 3959 * acos( cos( radians("+lat.to_s+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+lon+") ) + sin( radians("+lat.to_s+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores HAVING distance < " + distance.to_s + " ORDER BY distance LIMIT 50 ;");
		#@stores = Store.find_by_sql("SELECT *, ( 3959 * acos( cos( radians("+lat.to_s+") ) * cos( radians( lat ) ) * cos( radians( lon ) - radians("+lon+") ) + sin( radians("+lat.to_s+") ) * sin( radians( lat ) ) ) ) AS distance FROM stores ORDER BY distance ;");
		render :json => @store, root: false
	end


end
