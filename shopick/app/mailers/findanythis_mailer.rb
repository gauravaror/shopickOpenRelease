class FindanythisMailer < ApplicationMailer
  default from: "hiremath@shopick.co.in"
  default to: "gauravarora.daiict@gmail.com"

  def findthis_email(find1, user)
	@find = find1
	mail(to: user.email, subject: '#FindThis, Someone looking for you to locate a post or product!')
  end
end
