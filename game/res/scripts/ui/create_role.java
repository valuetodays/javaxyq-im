/**
 * 
 */
package ui;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.javaxyq.core.DataManager;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.CharacterUtils;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.profile.impl.ProfileManagerImpl;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.widget.Animation;

/**
 * ��Ϸ���˵�
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class create_role extends PanelHandler {
	
	private String character = "0003";
	private String roleName;
	private static String[] attr_points = {"physique", "magic", "strength", "durability", "agility"};
	private static String[] ���� = {"0001","0002","0003","0004"};
	private static String[] ħ�� = {"0005","0006","0007","0008"};
	private static String[] ���� = {"0009","0010","0011","0012"};
	private static int[] �����ʼ���Ե� = {10, 10, 10, 10, 10};
	private static int[] ħ���ʼ���Ե� = {12, 11, 11, 8, 8};
	private static int[] �����ʼ���Ե� = {12, 5, 11, 12, 10};
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		displayRoleInfo();
	}	
	
	/**
	 * �ж�����array�Ƿ����ֵvalue
	 * @return
	 */
	private static boolean inArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(value))return true;
		}
		return false;
	}
	
	private static void init_���Ե�(PlayerVO playerVo,int[] ��ʼ���Ե�){
		for(int i=0; i<attr_points.length;i++){
				try {
					PropertyUtils.setProperty(playerVo, attr_points[i], ��ʼ���Ե�[i]);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
		}
	}

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: select_role ");
	}	
	
	public void selectRole(ActionEvent evt) {
		character = evt.getArgumentAsString(0);
		displayRoleInfo();
	}
	
	public void goback(ActionEvent evt) {
		Panel dlg = helper.getDialog("select_role");
		helper.hideDialog(panel);
		helper.showDialog(dlg);
	}
	
	public void gonext(ActionEvent evt) {
		//TODO create profile
		TextField field = (TextField) panel.findCompByName("role_name");
		roleName = field.getText();
		if(roleName.trim().length() == 0) {
			helper.prompt("�������½����������д�����������", 3000);
			return;
		}
		try {
			ProfileManager profileManager = application.getProfileManager();
			DataManager dataManager = application.getDataManager();
			String filename = newProfileName();
			String sceneId = "1506";
			Profile profile = profileManager.newProfile(filename);
			PlayerVO playerVo = dataManager.createPlayerData(character);
			playerVo.setName(roleName);
			playerVo.setSceneLocation(new Point(38, 20));
			playerVo.setMoney(1500000);
			if(inArray(����,playerVo.character)) {			
				init_���Ե�(playerVo, �����ʼ���Ե�);
			}else if(inArray(ħ��,playerVo.character)) {
				init_���Ե�(playerVo, ħ���ʼ���Ե�);
			}else if(inArray(����,playerVo.character)) {
				init_���Ե�(playerVo, �����ʼ���Ե�);
			}
			dataManager.recalcProperties(playerVo);
			
			ItemInstance[] items = new ItemInstance[26];
			ItemInstance item = dataManager.createItem("��Ҷ��",99);
			addItem(items, item);
			item = dataManager.createItem("����",99);
			addItem(items, item);
			item = dataManager.createItem("Ѫɫ�軨",99);
			addItem(items, item);
			item = dataManager.createItem("�����",99);
			addItem(items, item);
			createWeapon(character, items);
			
			profile.setPlayerData(playerVo);
			profile.setSceneId(sceneId);
			profile.setItems(items);
			profileManager.saveProfile(profile);
			
			helper.prompt("���ﴴ���ɹ���", 3000);
			try {
				String profileName = profile.getName();
				application.loadProfile(profileName);
				application.enterScene();
			} catch (ProfileException e) {
				System.err.println("������Ϸ�浵ʧ��!");
				e.printStackTrace();
				helper.prompt("������Ϸ�浵ʧ��!", 3000);
			}
			
		} catch (ProfileException e) {
			e.printStackTrace();
		}
	}

	private void addItem(ItemInstance[] items, ItemInstance item) {
		for(int i=6;i<items.length;i++) {
			if(items[i] == null) {
				items[i] = item;
				break;
			}
		}
	}

	private String newProfileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(new Date());
	}

	private void displayRoleInfo() {
		Label label = (Label) panel.findCompByName("role_head");
		String filename = "/wzife/login/photo/selected/"+character+".tcp";
		Animation anim = SpriteFactory.loadAnimation(filename);
		if(anim != null) {
			label.setAnim(anim);
		}else {
			System.err.println("displayRoleInfo failed: "+filename);
		}
		//TODO ����������Ϣ
		
	}	
	
	private void createWeapon(String charName, ItemInstance[] items) {
		//��������
		String[] weaponNames = null;
		if(CharacterUtils.char_0001.equals(charName)) {
			weaponNames = new String[] {"��ͭ�̽�","��ң����"};
		}else if(CharacterUtils.char_0002.equals(charName)) {
			weaponNames = new String[] {"��Ҷ��"};
		}else if(CharacterUtils.char_0003.equals(charName)) {
		}else if(CharacterUtils.char_0004.equals(charName)) {
		}else if(CharacterUtils.char_0005.equals(charName)) {
		}else if(CharacterUtils.char_0006.equals(charName)) {
		}else if(CharacterUtils.char_0007.equals(charName)) {
		}else if(CharacterUtils.char_0008.equals(charName)) {
		}else if(CharacterUtils.char_0009.equals(charName)) {
			weaponNames = new String[] {"�廢�ϻ�"};
		}else if(CharacterUtils.char_0010.equals(charName)) {
			weaponNames = new String[] {"��ӧǹ","�廢�ϻ�","��ң����"};
		}else if(CharacterUtils.char_0011.equals(charName)) {
		}else if(CharacterUtils.char_0012.equals(charName)) {
		}
		
		for (int i = 0; (weaponNames != null) && (i<weaponNames.length); i++) {
			ItemInstance item = dataManager.createItem(weaponNames[i]);
			addItem(items, item);
		}
	}


}
