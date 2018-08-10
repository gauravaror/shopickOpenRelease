class UsercreatedMailer < ApplicationMailer
  default from: "hiremath@shopick.co.in"
  default to: "gauravarora.daiict@gmail.com"

  def usercreated_email(find1, user1)
	@usernew = User.find(find1);
	user = User.find(user1);
	mail(to: user.email, subject: 'Someone Downloaded the Shopick app just now!!!!')
  end
end
