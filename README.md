# jmeter_He_custom_sampler
Jmeter custom sampler for Helium datastore

This is a custom java sampler class that can be used to benchmark He datastore.

How to use

Build using maven

mvn package

extract zip into JMeter dir

run jmeter

sh ./bin/jmeter.sh -n -t *.jmx -l outfile.csv
