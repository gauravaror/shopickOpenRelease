class NotifyGenderJob
  	require 'gcm'
    include Sidekiq::Worker

  def perform(notification_id, gender)
	  gcm = GCM.new("AIzaSyAEbT8zhxyoJoL6H_YF2dJwOhe5Dv5QWEk")
    notification = Notification.find(notification_id)
    token = []
    #User.all.map { |e| token.push(e.gcm_token) }
      
      User.where(gender: gender).each_slice(100) { |batch|
        token = []
        batch.each do | element |
          token.push(element.gcm_token)
        end
        
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
        
    	#registration_ids= ["ft0uRgZUmhg:APA91bHxl_nyITF_iiPJlp9WR4YfkQaOfxph2eBZvCB_5_3McrBO70rrIA0GcfwtL4I-a8DOyn1LwK5j1ammKSnq2-qBtIMlh6L9Y_gvRG2K86a2JowNboI7eSMNhDLR5t1s5xqnuANj"] # an array of one or more client registration IDs
    	options = {data: { image: imageUrl, largeIcon: largeIcon ,intentUrl: intentUrl, title: title, message: message}, collapse_key: "updated_score"}
    	response = gcm.send(token, options)
      puts response
      sleep 2
    }
  end
end

