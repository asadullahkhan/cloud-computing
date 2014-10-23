import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;


public class MetaHumans {
	
	//ec2-23-23-8-126.compute-1.amazonaws.com
	
	private static Configuration conf = null;
    /**
     * Initialization
     */
    static {
        conf = HBaseConfiguration.create();
    }
	
	public static void main(String[] args) {
		String name = "MetaHumans";
        try {
			//conf.set("hbase.master","localhost:60000");
			 
			HBaseAdmin admin = new HBaseAdmin(conf);
			// new table called MetaHumans
			HTableDescriptor tableDesc = new HTableDescriptor(name); 
			// new column fam called background
			HColumnDescriptor bg = new HColumnDescriptor("background".getBytes());
			// new column fam called powers
			HColumnDescriptor pow = new HColumnDescriptor("powers".getBytes());
			
			tableDesc.addFamily(bg);
			tableDesc.addFamily(pow);
			if (!admin.tableExists(name))
				admin.createTable(tableDesc);
			
			HTable table = new HTable(conf, name);
			
			//read file at arg0
			FileInputStream fstream = new FileInputStream(args[0]);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line = br.readLine();
			
			while((line = br.readLine()) != null) {
				
				StringTokenizer st = new StringTokenizer(line);
				//int id = Integer.parseInt(st.nextToken());
				String id = st.nextToken();
				Put p = new Put(Bytes.toBytes(id));
				
				p.add(Bytes.toBytes("background"), Bytes.toBytes("id"), Bytes.toBytes(id));
				p.add(Bytes.toBytes("background"), Bytes.toBytes("gender"), Bytes.toBytes(st.nextToken()));
				p.add(Bytes.toBytes("background"), Bytes.toBytes("race"), Bytes.toBytes(st.nextToken()));
				p.add(Bytes.toBytes("background"), Bytes.toBytes("side"), Bytes.toBytes(st.nextToken()));
				//powers         
				//int value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("superspeed"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("superhearing"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("supervision"), Bytes.toBytes(st.nextToken()));
				///value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("flying"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("invisibility"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("icecontrol"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("superhealing"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("magicpowers"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("spidersense"), Bytes.toBytes(st.nextToken()));
				//value = Integer.parseInt(st.nextToken());
				p.add(Bytes.toBytes("powers"), Bytes.toBytes("superbrain"), Bytes.toBytes(st.nextToken()));
				//System.out.println("-------------------PUTTING "+line);
				table.put(p);
				
				
			}
				
			

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
