class UserMailer < ApplicationMailer
  default from: "hiremath@shopick.co.in"

  def welcome_email(user1)
	@user = User.find(user1);
	@url  = 'http://shopick.co.in';
	mail(to: @user.email, subject: 'Thank you for downloading SHOPICK. We want to gift you something in return!');
  end
end
