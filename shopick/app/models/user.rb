class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, 
         :recoverable, :rememberable, :trackable, :validatable
 # You likely have this before callback set up for the token.
  before_save :ensure_authentication_token
  has_many :posts
  has_many :find_thi
  has_many :affinity_scores
  has_many :read_posts

  has_many :referred_user
  accepts_nested_attributes_for :referred_user, :allow_destroy => true
  has_many :picks_history
  accepts_nested_attributes_for :picks_history, :allow_destroy => true

  has_and_belongs_to_many :posts,  :join_table => :posts_likes
  has_and_belongs_to_many :products,  :join_table => :products_likes
  has_and_belongs_to_many :tips,  :join_table => :tips_likes
  has_and_belongs_to_many :banners,  :join_table => :banners_likes
  has_and_belongs_to_many :brands,  :join_table => :brands_likes
  has_and_belongs_to_many :post_collections,  :join_table => :post_collections_likes
  has_and_belongs_to_many :brand_updates,  :join_table => :brand_updates_likes
  
  def ensure_authentication_token
    if authentication_token.blank?
      self.authentication_token = generate_authentication_token
    end
  end

  private
  
  def generate_authentication_token
    loop do
      token = Devise.friendly_token
      break token unless User.where(authentication_token: token).first
    end
  end

def rank
  User.where("monthlyPicks > ?", monthlyPicks).count + 1
end

def self.update_picks_histories(user_id)
        post_usr = User.find(user_id)
        signup_picks_overall = post_usr.picks_history.create(pickTransaction: 40,
        user_id: post_usr.id,
        pickType: 0,
        globalID:  SecureRandom.uuid,
        processed: false,
        notificationMessage: "Thanks for Signing up on shopick. We have credited your picks into your account.",
        transactionReason: "Add the picks for signing up on shopick: "+ post_usr.id.to_s)

      signup_picks_monthly = post_usr.picks_history.create(pickTransaction: 40,
        user_id: post_usr.id,
        pickType: 1,
        processed: false,
        globalID:  SecureRandom.uuid,
        notificationMessage: "Thanks for Signing up on shopick. We have credited your monthly campaign picks into your account.",
       transactionReason: "Add the monthly picks for signing up on shopick : " + post_usr.id.to_s)
      post_usr.save
      PicksHistory.approve_pick_history(signup_picks_overall)
      PicksHistory.approve_pick_history(signup_picks_monthly)
      User.send_admin_users_notification(" signing up on shopick added picks  : "+ post_usr.email+ " user id "+ post_usr.id.to_s,
        "http://shopick.co.in/admin/users?utf8=%E2%9C%93&q%5Bemail_contains%5D="+post_usr.email+"&commit=Filter&order=id_desc")
end


def self.send_admin_users_notification(subject, message)
  User.where(admin: true).each do |user|
    MessageMailer.message_email(subject, message, user).deliver_later
  end
end


def self.send_admin_users_findthis_notification(findthis)
  User.where(admin: true).each do |user|
    FindanythisMailer.findthis_email(findthis, user).deliver_later
  end
end
  include Elasticsearch::Model
  include Elasticsearch::Model::Callbacks
 settings  :analysis => {
              :filter => {
                :title_ngram  => {
                  "type"      => "edgeNGram",
                  "min_gram"  => 2,
                  "max_gram"  => 8,
                   }
              },
              :analyzer => {
                :default => {
                  "tokenizer"    => "standard",
                  "type"         => "custom",
                   "filter" => [ "lowercase", "asciifolding", "title_ngram" ]}
              }
            } do
    mapping  do
    end
  end

User.__elasticsearch__.create_index!
User.import


end
