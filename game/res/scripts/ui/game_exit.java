/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui;

import java.awt.Desktop;
import java.net.URI;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.ui.Button;

/**
 * �˳���Ϸ�Ի���ű�
 * @author dewitt
 * @date 2009-11-27 create
 */
public class game_exit extends PanelHandler {
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: system.mainwin ");
	}
	
	public void exit_game(ActionEvent evt) {
		//application.shutdown();
		application.endGame();
	}
	public void visit_homepage(ActionEvent evt) {
		try {
			Desktop.getDesktop().browse(new URI(GameMain.getHomeURL()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void contributors(ActionEvent evt) {
		helper.showDialog("contributors");
	}
	
	public void toggle_debug(ActionEvent evt) {
		application.setDebug(!application.isDebug());
		application.getScriptEngine().setDebug(application.isDebug());
		Button btn = (Button) panel.findCompByName("debugbtn");
		if(application.isDebug()) {
			System.out.println("�Ѵ���Ϸ����");
			btn.setText("�رյ���");
		}else {
			System.out.println("�ѹر���Ϸ����");
			btn.setText("�򿪵���");
		}
	}
	
	public void toggle_music(ActionEvent evt) {
		GameMain.setPlayingMusic(!GameMain.isPlayingMusic());
		Button btn = (Button) panel.findCompByName("musicbtn");
		if(GameMain.isPlayingMusic()) {
			btn.setText("�ر�����");
			System.out.println("����Ϸ��������");
		}else {
			btn.setText("������");
			System.out.println("�ر���Ϸ��������");
		}
	}
	
	public void saveProfile(ActionEvent evt) {
		try {
			application.saveProfile();
		} catch (ProfileException e) {
			e.printStackTrace();
			application.getUIHelper().prompt("������Ϸʧ�ܣ�", 3000);
		}
	}
}
