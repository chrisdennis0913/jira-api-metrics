require 'resolv'

begin
  Resolv.getaddress('nexus-gss.uscis.dhs.gov')
  source 'https://nexus-gss.uscis.dhs.gov/nexus/content/repositories/' \
         'VIS-Rubygems-Proxy'
  source 'https://nexus-gss.uscis.dhs.gov/nexus/content/repositories/' \
         'VIS-rubygems-internal'
rescue Resolv::ResolvError
  source 'https://rubygems.org'
end

gem 'rubocop'
gem 'serverspec'
gem 'uscis-pipeline'
gem 'vis_sparklepack'
