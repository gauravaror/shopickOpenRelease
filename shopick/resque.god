rails_env   = ENV['RAILS_ENV']  || "production"
rails_root  = ENV['RAILS_ROOT'] || "/Users/gaurav/AcquireTech/shopick"
num_workers = rails_env == 'production' ? 5 : 2

num_workers.times do |num|
  God.watch do |w|
    w.dir      = "#{rails_root}"
    w.name     = "notifier-#{num}"
    w.group    = 'notifier'
    w.interval = 30.seconds
    w.behavior(:clean_pid_file)
    w.env      = {"QUEUE"=>"notifier", "RAILS_ENV"=>rails_env}
    w.start    = "/usr/local/bin/rake -f #{rails_root}/Rakefile environment resque:work QUEUE=*"

  end
end
