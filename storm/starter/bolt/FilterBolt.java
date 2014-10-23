package storm.starter.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;




public class FilterBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector)
	{
		int num_retweets = tuple.getInteger(1);
		int num_likes = tuple.getInteger(2);
		String geo_location = tuple.getString(3);
		
		if(num_retweets < 4 || num_likes < 8)
			System.out.println("NUM RETWEETS: " + num_retweets + " , " + num_likes + " , " + geo_location + " --> FILTERED OUT");
		else
		{
			System.out.println("NUM RETWEETS: " + num_retweets + " , " + num_likes + " , " + geo_location + " --> FILTERED IN");
			collector.emit(new Values(num_retweets, num_likes, geo_location));
		}
		   
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd)
	{
		ofd.declare(new Fields("num_retweets", "num_likes", "geo_location"));
    }
    
}
