require 'docker'
require 'serverspec'

describe 'Dockerfile' do
  before(:all) do
    build_dir = 'containers/vis_jira_metrics'
    image_id = ENV['DOCKER_IMAGE_ID_vis_jira_metrics']
    if image_id.nil?
      image_id = Docker::Image.build_from_dir(build_dir)
                              .id
    end

    set :os, family: :debian
    set :backend, :docker
    set :docker_image, image_id
  end

  describe file('/opt') do
    it { should be_directory }
    it { should exist }
  end

  %w[oracle-java8-installer].each do |package_name|
    describe package(package_name) do
      it { should be_installed }
    end
  end

  describe command('ls -al /opt') do
    its(:stdout) { should match(/jira-metrics.+\.jar/) }
  end
end
