class FirstNotificationJob
  	require 'gcm'
    include Sidekiq::Worker

  def perform(notification_id, user_id)
	gcm = GCM.new("AIzaSyAEbT8zhxyoJoL6H_YF2dJwOhe5Dv5QWEk")
  notification = Notification.find(notification_id)
    token = []
    user = User.find(user_id)
    token.push(user.gcm_token)
  
    imageUrl = notification.image_url.url(:main);
    largeIcon = notification.image_url.url(:thumb)
    title = notification.custom_title;
    message = notification.custom_message;
    intentUrl = notification.intentUrl;

    if notification.brand_update
      imageUrl = notification.brand_update.update_background.url(:medium);
      largeIcon = notification.brand_update.update_background.url(:thumb);
    elsif notification.post
      imageUrl = notification.post.image.url(:medium);
      largeIcon = notification.post.image.url(:thumb);
    end
    
	
	options = {data: { image: imageUrl, largeIcon: largeIcon ,intentUrl: intentUrl, title: title, message: message}, collapse_key: "updated_score"}
	response = gcm.send(token, options)
    puts response
    sleep 2
  end
end

