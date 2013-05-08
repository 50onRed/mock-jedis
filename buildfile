#!/usr/bin/env ruby

THIS_VERSION = "0.1.1"

repositories.remote << 'http://mirrors.ibiblio.org/pub/mirrors/maven2'

JEDIS = transitive('redis.clients:jedis:jar:2.1.0')

define 'mock-jedis' do
    project.version = THIS_VERSION
    compile.with JEDIS
    doc.using(:windowtitle => "MockJedis", :private => true)
    package(:jar).with(
        :manifest=>manifest.merge({
                "Class-Path" => compile.dependencies.map {
                    |item| "lib/" + File.basename(item.to_s)
                }.join(" ")
            }
        ))
    package.enhance do
      mkdir_p _(:target, :lib)
      cp compile.dependencies.collect { |d| d.to_s }, _(:target, :lib)
    end
end
