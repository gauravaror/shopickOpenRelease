class PicksHistory < ActiveRecord::Base
	belongs_to :user

	def self.approve_pick_history(picks_history)
		if  picks_history.processed == false
			concernUser = User.find(picks_history.user_id)
			#user = User.find_by(:email => "gauravarora.daiict@gmail.com")
		    #user2 = User.find_by(:email => "hiremath.in@gmail.com")
		    picks_history.approved = true;
		    picks_history.processed = true;
		    picks_history.save
		    if picks_history.pickType == 0
		    	concernUser.picks  = concernUser.picks + picks_history.pickTransaction
		    	concernUser.save
		    else
		    	concernUser.monthlyPicks  = concernUser.monthlyPicks + picks_history.pickTransaction
		    	concernUser.save
		    end
		    PicksHistoryNotificationJob.perform_in(10.seconds, picks_history.pickTransaction.to_s + " Picks", picks_history.notificationMessage, concernUser.id)
			 return  { 
				 	"status" => "200",
				 	"message" => "Notified the user" }.to_json
		else 
			 return { 
		 	"status" => "400",
		 	"message" => "Already Notified ask admin to make approval null if you want this.." }.to_json

		end
	end

	def self.reject_pick_history(picks_history)
		if  picks_history.processed == false
			concernUser = User.find(picks_history.user_id)
		    picks_history.approved = false;
		    picks_history.processed = true;
		    picks_history.save
		    PicksHistoryNotificationJob.perform_in(10.seconds, picks_history.rejectionTitle, picks_history.notificationMessage,concernUser.id)
			 return  { 
				 	"status" => "200",
				 	"message" => "Notified the user" }.to_json
		else 
			 return  { 
		 	"status" => "400",
		 	"message" => "Already Notified ask admin to make approval null if you want this.." }.to_json

		end
	end
end
