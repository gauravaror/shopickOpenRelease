class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception
  before_filter :authenticate_user_from_token!
  protect_from_forgery with: :null_session, if: Proc.new { |c| c.request.format == 'application/json' }


  # This is Devise's authentication
  before_filter :authenticate_user!

    private
  
  # For this example, we are simply using token authentication
  # via parameters. However, anyone could use Rails's token
  # authentication features to get the token from a header.
  def authenticate_user_from_token!
    user_email = params[:user_email].presence
    user       = user_email && User.find_by_email(user_email)

    # Notice how we use Devise.secure_compare to compare the token
    # in the database with the token given in the params, mitigating
    # timing attacks.

    if user && Devise.secure_compare(user.authentication_token, params[:user_token])
      sign_in user, store: false
    end
  end

 def authenticate_admin_user!
  raise SecurityError unless current_user.try(:admin?)
 end

rescue_from SecurityError do |exception|
  redirect_to root_url
end

end
