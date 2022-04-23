package phantom.task;

import java.util.List;

import phantom.FakePlayerTaskManager;

import net.sf.l2j.gameserver.ThreadPoolManager;

public class AITaskRunner implements Runnable
{	
	@Override
	public void run()
	{		
		FakePlayerTaskManager.INSTANCE.adjustTaskSize();
		List<AITask> aiTasks = FakePlayerTaskManager.INSTANCE.getAITasks();		
		aiTasks.forEach(aiTask -> ThreadPoolManager.getInstance().executeAi(aiTask));
	}	
}