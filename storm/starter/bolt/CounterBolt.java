package storm.starter.bolt;

import backtype.storm.Config;
import backtype.storm.task.IOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

import storm.starter.util.TupleHelpers;




public class CounterBolt extends BaseBasicBolt
{
	private int totalEurope = 0, totalAP = 0, totalUS = 0;
	private static final int EMIT_FREQUENCY_IN_SECONDS = 5;
	private int totalCount = 0;
	
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector)
	{
		
		//int num_retweets = tuple.getInteger(0);
		//int num_likes = tuple.getInteger(1);
		String geo_location = tuple.getString(2);
		
		totalCount++;
		
		if(geo_location.equals("Europe"))
			totalEurope++;
		else if(geo_location.equals("Asia/Pacific"))
			totalAP++;
		else if(geo_location.equals("US"))
			totalUS++;
		else{};
		
		System.out.println("COUNTER BOLT: total--> " +  totalCount + " , " + totalEurope + " , " + totalUS + " , " + totalAP);
		
		
    	if (TupleHelpers.isTickTuple(tuple))
		{
    		collector.emit(new Values(totalCount, totalEurope, totalUS, totalAP));
    	}
    }
	
    @Override
    public void declareOutputFields(OutputFieldsDeclarer ofd)
	{
		ofd.declare(new Fields("totalCount", "totalEurope", "totalUS", "totalAP"));
    }
    
    @Override
    public Map<String, Object> getComponentConfiguration()
	{
        Map<String, Object> conf = new HashMap<String, Object>();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, EMIT_FREQUENCY_IN_SECONDS);
        return conf;
    }
    
}
