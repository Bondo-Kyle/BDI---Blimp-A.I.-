import java.util.ArrayList;
import java.util.Random;

import cs480viewer.task5.Agent;
import cs480viewer.task5.AgentManager;
import cs480viewer.task5.E_TrackMode;
import cs480viewer.task5.Viewer;

//=============================================================================================================================================================
public class CS480Task5Template
{
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(final String[] args)
	{
		new CS480Task5Template();
	}

	private final Viewer _viewer;

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------
	public CS480Task5Template()
	{
		_viewer = new Viewer("task5_3.trk", 0);
		AgentManager agentManager = _viewer.getAgentManager();

		Agent agentBlimpActual = agentManager.getAgent("blimp-actual");
		Agent agentBlimpBelieved = agentManager.getAgent("blimp-believed");

		agentBlimpActual.setTrackMode(E_TrackMode.HORIZONTAL);
		agentBlimpBelieved.setTrackMode(E_TrackMode.HORIZONTAL);
		// }

		_viewer.doAddEvent("station1", 0, 0, -500, 0, 0, 0);
		_viewer.doAddEvent("station2", -500, 0, 500, 0, 0, 0);
		_viewer.doAddEvent("ray1", 0, 0, -500, 0, 0, 0);
		_viewer.doAddEvent("ray2", -500, 0, 500, 0, 0, 0);
		_viewer.doAddEvent("blimp-actual", -200, 0, 0, 0, 0, 0);
		_viewer.doAddEvent("blimp-believed", -200, 0, 0, 0, 0, 0);
		_viewer.doAdvanceEventClock();

		do
		{
			ArrayList<float []> ray1 = sendRay1();
			ArrayList<float []> ray2 = sendRay2();
			float [] intersect = checkForIntersection(ray1, ray2);
			moveBlimp(intersect);
			_viewer.doAdvanceEventClock();

		}while(true);
		// doTest1();
	}

	public float [] checkForIntersection(ArrayList<float []> ray1,ArrayList<float []> ray2)
	{

		float [] intersect = new float[3];

		int end = 0;

		if(ray1.size() > ray2.size())
		{
			end = ray2.size();
		}
		else if(ray1.size() < ray2.size())
		{
			end = ray1.size();
		}
		else
			end = ray1.size();

		for(int i = 0; i < end; i++)
		{
			if(inRange(ray1.get(i), ray2.get(i)) == true)
			{
				intersect[0] = -ray1.get(i)[0]*50;
				intersect[2] = ray1.get(i)[2]*50;
			}
		}
		return intersect;
	}

	public boolean inRange(float [] ray1, float [] ray2)
	{


		if(ray1[0] >= ray2[0]-10 && ray1[0] <= ray2[0]+10)
		{

			if(ray1[2] >= ray2[2]-10 && ray1[2] <= ray2[2]+10)
			{
				return true;
			}
		}




		return false;
	}
	private void moveBlimp(float [] coords)
	{

		Agent agentBlimpActual = _viewer.getAgentManager().getAgent("blimp-actual");
		Agent agentBlimpBelieved = _viewer.getAgentManager().getAgent("blimp-believed");
		double yaw = Math.abs(agentBlimpActual.getAttitude().getYaw());
		double newX = 0.0;
		double newZ = 0.0;


		double believedX = 0.0;
		double believedZ = agentBlimpBelieved.getPosition().getZ()*50;
		double currentZ = _viewer.getAgentManager().getAgent("blimp-actual").getPosition().getZ()*50;

		if(yaw == 0)
		{
			if(coords[0] != 0)
			{
				if(coords[0] > -200) //-199 to 0
				{
					newX = -(coords[0]- -200)*1.01; // My believed position is greater than 200, so subtract from believed
					believedX = -200;
				}
				else if(coords[0] < -200) // -200 to -500
				{
					newX = -200 - coords[0]*1.01; // My believed position is less than 200, so subtract 200 from believed
					believedX = -200;
				}

				if(coords[2] > 200) // z is greater than 200, rotate to the right 90 degree
				{	
					yaw = 90;
					believedZ = 190;

				}

			}
			_viewer.doAdvanceEventClock();
			_viewer.doAddEvent("blimp-believed", believedX, 0, believedZ+ 10, yaw, 0, 0);

			_viewer.doAddEvent("blimp-actual", coords[0] + newX, 0, currentZ+ 10, yaw, 0, 0);
			_viewer.doAdvanceEventClock();
		}
		else if(yaw == 90)
		{
			believedZ = agentBlimpBelieved.getPosition().getZ()*50;
			believedX = -agentBlimpBelieved.getPosition().getX()*50;
			currentZ = _viewer.getAgentManager().getAgent("blimp-actual").getPosition().getZ()*50;
			if(coords[0] != 0)
			{
				if(coords[2] > 200) // [200, inf)
				{
					newZ = -(coords[2]- 200)*1.01; // My believed position is greater than 200, so subtract from believed
				}
				else if(coords[2] < 200) // 
				{
					newZ = (200 - coords[2])*1.01; // My believed position is less than 200, so subtract 200 from believed
				}

				if(coords[0] > 200) // x is greater than 200, rotate to the right 90 degree
				{	
					yaw = 180;
				}
				believedZ = 200;

			}
			_viewer.doAdvanceEventClock();
			_viewer.doAddEvent("blimp-believed", believedX +20, 0, believedZ, yaw, 0, 0);
			_viewer.doAddEvent("blimp-actual", coords[0] + 20, 0, currentZ+ newZ, yaw, 0, 0);
			_viewer.doAdvanceEventClock();
		}
		else if(yaw == 180)
		{
			believedZ = agentBlimpBelieved.getPosition().getZ()*50;
			believedX = -agentBlimpBelieved.getPosition().getX()*50;
			currentZ = _viewer.getAgentManager().getAgent("blimp-actual").getPosition().getZ()*50;
			if(coords[0] != 0)
			{
				if(coords[0] > 200) //-199 to 0
				{
					newX = -(coords[0]- 200)*1.01; // My believed position is greater than 200, so subtract from believed
					believedX = 200;
				}
				else if(coords[0] < 200) // -200 to -500
				{
					newX = 200 - coords[0]*1.01; // My believed position is less than 200, so subtract 200 from believed
					believedX = 200;
				}

				if(coords[2] < -200) // z is greater than 200, rotate to the right 90 degree
				{	
					yaw = 270;
					believedZ = -190;

				}
				_viewer.doAdvanceEventClock();
				_viewer.doAddEvent("blimp-believed", believedX, 0, believedZ- 10, yaw, 0, 0);
				_viewer.doAddEvent("blimp-actual", coords[0] + newX, 0, currentZ- 10, yaw, 0, 0);
				_viewer.doAdvanceEventClock();
			}

		}
		else if(yaw == 270)
		{
			believedZ = agentBlimpBelieved.getPosition().getZ()*50;
			believedX = -agentBlimpBelieved.getPosition().getX()*50;
			currentZ = _viewer.getAgentManager().getAgent("blimp-actual").getPosition().getZ()*50;
			if(coords[0] != 0)
			{
				if(coords[2] > -200) // [200, inf)
				{
					newZ = -(coords[2]- -200)*1.01; // My believed position is greater than 200, so subtract from believed
				}
				else if(coords[2] < -200) // 
				{
					newZ = (-200 - coords[2])*1.01; // My believed position is less than 200, so subtract 200 from believed
				}

				if(coords[0] < -200) // x is greater than 200, rotate to the right 90 degree
				{	
					yaw = 0;
				}
				believedZ = -200;

			}
			_viewer.doAdvanceEventClock();
			_viewer.doAddEvent("blimp-believed", believedX -20, 0, believedZ, yaw, 0, 0);
			_viewer.doAddEvent("blimp-actual", coords[0] - 20, 0, currentZ+ newZ, yaw, 0, 0);
			_viewer.doAdvanceEventClock();
		}


	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	private ArrayList<float []> sendRay1() //one clock iteration, returns an ArrayList of all bugs in the current area.
	{
		Agent blimpActual = _viewer.getAgentManager().getAgent("blimp-actual");
		Agent blimpBelieved = _viewer.getAgentManager().getAgent("blimp-believed");
		float blimpBelievedX = blimpBelieved.getPosition().getX();
		float blimpBelievedZ = blimpBelieved.getPosition().getZ();

		float blimpActualX = -blimpActual.getPosition().getX()*50;
		float blimpActualZ = blimpActual.getPosition().getZ()*50;

		Agent agentRay1 = _viewer.getAgentManager().getAgent("ray1");
		float x = -agentRay1.getPosition().getX()*50;
		float z = agentRay1.getPosition().getZ()*50;
		ArrayList<float []> endPoints = new ArrayList<float []>();

		for(int i = 45; i < 135; i++)
		{
			_viewer.doAddEvent("ray1", 0, 0, -500, i, 0, 0);
			//_viewer.doAdvanceEventClock();

			for(int j = 0; j < 725; j+=5)
			{
				int angle = i;

				float deltaX = (float) Math.cos(Math.toRadians(angle))*5;
				float deltaZ = (float) Math.sin(Math.toRadians(angle))*5;

				AgentManager agentManager = _viewer.getAgentManager();

				x = -agentManager.getAgent("ray1").getPosition().getX()*50;
				z = agentManager.getAgent("ray1").getPosition().getZ()*50;

				if(Math.abs(x) > 500 || Math.abs(z) > 500)
					break;
				Random r = new Random();
				double error = r.nextDouble()*2;
				error -= .75;
				_viewer.doAddEvent("ray1", (x+deltaX)+error, 0, (z+deltaZ) + error, angle, 0, 0);

				x = -agentRay1.getPosition().getX()*50;
				z = agentRay1.getPosition().getZ()*50;

				if(x >= blimpActualX-10 && x <= blimpActualX+10)
				{
					if(z >= blimpActualZ-10 && z <= blimpActualZ+10)
					{
						endPoints.add(_viewer.getAgentManager().getAgent("ray1").getPosition().getAll());
						//System.out.println("FOUND BLIMP FROM RAY 1");
						break;
					}
				}
				//_viewer.doAdvanceEventClock();

			}
		}
		return endPoints;
	}



	private ArrayList<float []> sendRay2() //one clock iteration, returns an ArrayList of all bugs in the current area.
	{
		Agent blimpActual = _viewer.getAgentManager().getAgent("blimp-actual");
		Agent blimpBelieved = _viewer.getAgentManager().getAgent("blimp-believed");

		float blimpActualX = -blimpActual.getPosition().getX()*50;
		float blimpActualZ = blimpActual.getPosition().getZ()*50;

		Agent agentRay2 = _viewer.getAgentManager().getAgent("ray2");

		float x = -agentRay2.getPosition().getX()*50;
		float z = agentRay2.getPosition().getZ()*50;

		ArrayList<float []> endPoints = new ArrayList<float []>();

		for(int i = 270; i < 360; i++)
		{
			_viewer.doAddEvent("ray2", -500, 0, 500, 0, 0, 0);
			//_viewer.doAdvanceEventClock();


			for(int j = 0; j < 1000 ; j+=5)
			{
				int angle = i;

				float deltaX = (float) Math.cos(Math.toRadians(angle))*5;
				float deltaZ = (float) Math.sin(Math.toRadians(angle))*5;

				AgentManager agentManager = _viewer.getAgentManager();

				x = -agentManager.getAgent("ray2").getPosition().getX()*50;
				z = agentManager.getAgent("ray2").getPosition().getZ()*50;

				if(Math.abs(x) > 500 || Math.abs(z) > 500)
					break;
				Random r = new Random();
				double error = r.nextDouble()*2;
				error -= .75;
				_viewer.doAddEvent("ray2", (x+deltaX)+error, 0, (z+deltaZ) + error, angle, 0, 0);
				x = -agentRay2.getPosition().getX()*50;
				z = agentRay2.getPosition().getZ()*50;

				if(x >= blimpActualX-10 && x <= blimpActualX+10)
				{
					if(z >= blimpActualZ-10 && z <= blimpActualZ+10)
					{
						endPoints.add(_viewer.getAgentManager().getAgent("ray2").getPosition().getAll());
						//System.out.println("FOUND BLIMP FROM RAY 2");
						break;
					}
				}
				//_viewer.doAdvanceEventClock();
			}
		}
		return endPoints;
	}


}
