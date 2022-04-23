package net.sf.l2j.gameserver.model.entity.events.achievments;

import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.model.entity.events.achievments.base.Condition;

/**
 * @author Matim
 * @version 1.0
 */
public class Castle extends Condition
{
       public Castle(Object value)
       {
               super(value);
               setName("Have Castle");
       }

       @Override
       public boolean meetConditionRequirements(L2PcInstance player)
       {
               if (getValue() == null)
                       return false;
              
               if (player.getClan() != null)
               {
                       if (player.isCastleLord(5) || player.isCastleLord(3) || player.isCastleLord(7))
                               return true;
               }
               return false;
       }
}