/*
 * JavaXYQ NPC Scripts
 * home page: http://javaxyq.googlecode.com
 */

package npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.javaxyq.action.Actions;
import com.javaxyq.battle.BattleEvent;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;
import com.javaxyq.model.Task;
import com.javaxyq.widget.Player;


/**
 * @author gongdewei
 * @date 2014-03-02 create
 */
public class n1008 extends PlayerAdapter {
	
	private Random rand = new Random();
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	
    	String chat="��Ϊ��ׯ��ϯ���ӣ���һ������ʦ��ʹ�����ҿ��԰�������ϰ���գ������Թ����������ز��";   
    	Option[] options = new Option[5];
    	options[0] = new Option("��ϰ���գ�������","practice","1");
    	options[1] = new Option("��ϰ���գ��м���","practice","2");
    	options[2] = new Option("��ϰ���գ��߼���","practice","3");
    	options[3] = new Option("���������ز�","buy");
    	options[4] = new Option("��Ҳ��ȥ","close");
    	
    	Option result = doTalk(evt.getPlayer(),chat,options);
		if(result!=null) {
			if("practice".equals(result.getAction())) {
				practice(evt);
			}else if("buy".equals(result.getAction())) {
				buy(evt);
			}
		}
    	
    	System.out.println("result: "+result);
    }
	
    /**
     * ����
     * @param evt
     */
    public void practice(PlayerEvent evt) {
    	Task task = new Task();
    	patrol(task);
//    	while(!task.isFinished()) {
//    	}
    }
    
    /**
     * ���������ز�
     * @param evt
     */
    public void buy(PlayerEvent evt) {
    	
    }
    
	private boolean patrol(final Task task) {
		//System.out.println("patrol $task");
		final Player player = context.getPlayer();
		player.stop(true);
		//��ʼ��С�ֶ���
		int level = context.getPlayer().getData().getLevel();
		List<Player> t1 = new ArrayList<Player>();
		List<Player> t2 = new ArrayList<Player>();
		String[] elfs = {"2036","2037","2009","2010","2011","2012"};
		String[] elfNames = {"�󺣹�","����","ܽ������","����","��������","����"};
		Random random = new Random();
		final int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			t2.add(dataManager.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//����ս��
		ApplicationHelper.getApplication().doAction(this, Actions.ENTER_BATTLE,new Object[] {t1,t2});
		int rounds = task.getInt("battle_rounds");
		helper.prompt("ս��׼������"+(rounds==0?"һ":"��")+"�غϿ�ʼ��~~!",3000);
		window.addBattleListener(new BattleListener() {
			//ս��ʤ������
			public void battleWin(BattleEvent e) {
				System.out.println("ս��ʤ��");
				task.add("battle_rounds",1);
				task.add("battle_win",1);
				int exp = player.getData().level*(1000 + rand.nextInt(200))*elfCount/10;
				player.getData().exp += exp;
				helper.prompt("ս��ʤ�������"+exp+"�㾭�顣",3000);
				battleCompelete();
			}
			//ս��ʧ�ܴ���
			public void battleDefeated(BattleEvent e) {
				System.out.println("ս��ʧ��");
				task.add("battle_rounds",1);
				//��ѪΪ0������ָ�һ����Ѫ
				if(player.getData().hp ==0) {
					player.getData().hp = 100;
				}
				helper.prompt("�벻���ҵ����ջ�����ôһ��~~!",3000);
				battleCompelete();
			}
			public void battleTimeout(BattleEvent e) {
			}
			public void battleBreak(BattleEvent e) {
			}
			
			private void battleCompelete() {
				if(task.getInt("battle_rounds") >= 2) {
					task.setFinished(true);
					if(task.getInt("battle_rounds") == task.getInt("battle_win")) {
						helper.prompt("��ϲ����ȫʤ��ʵ��������",3000);
					}else {
						helper.prompt("�ڼ����䣬���ܰ�ս��ʤ��",3000);
					}
				}
			}
		});
		return true;
	}

}
