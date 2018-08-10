class PicksHistoryNotificationJob
  	require 'gcm'
    include Sidekiq::Worker

  def perform(title, message, user_id)
	gcm = GCM.new("AIzaSyAEbT8zhxyoJoL6H_YF2dJwOhe5Dv5QWEk")
    token = []
    #notification.users.map { |e| token.push(e.gcm_token) }
    user = User.find(user_id)
    token.push(user.gcm_token)
  
    intentUrl = "android://www.shopick.co.in/display_picks";
    
	options = {data: { image: "", largeIcon: "" ,intentUrl: intentUrl, title: title, message: message}, collapse_key: "updated_score"}
	response = gcm.send(token, options)
    puts response
    sleep 2
  end
end

