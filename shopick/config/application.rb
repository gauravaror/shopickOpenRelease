require File.expand_path('../boot', __FILE__)

require 'rails/all'

# Require the gems listed in Gemfile, including any gems
# you've limited to :test, :development, or :production.
Bundler.require(:default, Rails.env)

module Shopick
  class Application < Rails::Application
    # Settings in config/environments/* take precedence over those specified here.
    # Application configuration should go into files in config/initializers
    # -- all .rb files in that directory are automatically loaded.
    config.assets.paths << Rails.root.join("vendor","assets","bower_components")
    config.assets.paths << Rails.root.join("vendor","assets","bower_components","bootstrap-sass-official","assets","fonts")
    config.assets.precompile << %r(.*.(?:eot|svg|ttf|woff|woff2)$)
    config.assets.precompile += %w( jquery-1.10.2.min.js rails.validations.js 
    rails.validations.simple_form.js move-top.js easing.js notify.js jquery.geocomplete.min.js 
    angular/angular.min.js angular-route/angular-route.min.js angular-resource/angular-resource.min.js 
    angular-material/angular-material.min.js angular-messages/angular-messages.min.js angular-aria/angular-aria.min.js 
    angular-animate/angular-animate.min.js modernizr.js findanythingbody.css shopick.module.js shopick-side-nav/shopick-side-nav.component.js 
    latest_launch/latest_launch.component.js offer/offer.component.js liked_collection/liked-collection.component.js 
    post/post.component.js  collection/collection-detail.component.js  product/product.component.js  updates/updates.component.js  
    browser_post.js  liked_collection/post.component.js cover4.png
    slick.js  style.css  slick.css completefindanything.css browser_post.css findpath.css angular-material/angular-material.css bootstrap/js/carousel.js)
    # Set Time.zone default to the specified zone and make Active Record auto-convert to this zone.
    # Run "rake -D time" for a list of tasks for finding time zone names. Default is UTC.
    # config.time_zone = 'Central Time (US & Canada)'
    config.autoload_paths += %W(#{config.root}/lib) # add this line
    # The default locale is :en and all translations from config/locales/*.rb,yml are auto loaded.
    # config.i18n.load_path += Dir[Rails.root.join('my', 'locales', '*.{rb,yml}').to_s]
    # config.i18n.default_locale = :de
    config.active_job.queue_adapter = :sidekiq

  end
end
