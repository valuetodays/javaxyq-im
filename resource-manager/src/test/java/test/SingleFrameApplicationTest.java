package test;

import com.javaxyq.tools.*;
import com.jidesoft.action.CommandBar;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * 想要正常显示出一个窗口，需要写一个类，继承{@link SingleFrameApplication}
 * 同时在，包名/resources/下新建一个properties文件，名称必须是“类名.properties”
 * @author liulei-home
 * @since 2018-10-03 11:36
 */
public class SingleFrameApplicationTest extends SingleFrameApplication {
    private static final Logger LOG = Logger.getLogger(SingleFrameApplicationTest.class);

    private JMenuBar menuBar;

    private JPanel topPanel;

    private JMenu editMenu;

    private JMenu fileMenu;

    private JButton openButton;

    private JPanel toolBarPanel;

    private FolderChooser folderChooser;

    private File lastOpenDir = new File("E:/Games/梦幻西游");
    private File lastSaveDir = new File(".");

    private JFileChooser fileChooser = new JFileChooser();

    private StatusBar statusBar;

    private JDesktopPane desktop;

    private JTree structTree;

    private PreviewPanel defaultPreviewPanel;

    private SingleFrameApplication app;

    private java.util.List<String> recentList;


    private Map<FileSystem, JTree> treeMap;

    private Cursor handCursor;

    private Cursor grabCursor;

    private JScrollPane treePanel;

    private JMenu helpMenu;

    private JFileChooser savefileChooser;

    private JideButton openFolderButton;

    private JMenu windowMenu;

    private SpriteExtractor spriteExtractor;

    public static void main(String[] args) {
        XYQTools.verifyJideLicense();
        launch(SingleFrameApplicationTest.class, args);
    }


    private ActionMap getAppActionMap() {
        return Application.getInstance().getContext().getActionMap(this);
    }

    private javax.swing.Action getAction(String key) {
        return getAppActionMap().get(key);
    }

    @Override
    protected void startup() {
        initGUI();
        app = this;
        FrameView frameView = getMainView();
        JFrame mainFrame = frameView.getFrame();
        ResourceMap resourceMap = getContext().getResourceMap();
        Integer w = resourceMap.getInteger("Application.width");
        Integer h = resourceMap.getInteger("Application.height");
        LOG.debug("w="+w + ", h="+h);
        w = w == null ? 640 : w;
        h = h == null ? 480 : h;
        mainFrame.setSize(w, h);
        show(frameView);
        mainFrame.setLocationRelativeTo(null);

        ImageIcon handIcon = app.getContext().getResourceMap().getImageIcon("handIcon");
        LOG.debug(handIcon);
    }

    private void initGUI() {
        topPanel = new JPanel();
        BorderLayout panelLayout = new BorderLayout();
        topPanel.setLayout(panelLayout);
        topPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        {
            toolBarPanel = new JPanel();
            topPanel.add(toolBarPanel, BorderLayout.NORTH);
            BorderLayout jPanel1Layout = new BorderLayout();
            toolBarPanel.setLayout(jPanel1Layout);
            {
                CommandBar toolBar = new CommandBar();
                toolBar.setFloatable(true);
                toolBarPanel.add(toolBar, BorderLayout.CENTER);
//				{
//					newButton = new JideButton();
//					toolBar.add(newButton);
//					newButton.setAction(getAction("newFile"));
//					newButton.setName("newButton");
//					newButton.setFocusable(false);
//				}
                {
                    openButton = new JideButton();
                    toolBar.add(openButton);
                    openButton.setAction(getAction("open"));
                    openButton.setName("openButton");
                    openButton.setFocusable(false);
                }
                {
                    openFolderButton = new JideButton();
                    toolBar.add(openFolderButton);
                    openFolderButton.setAction(getAction("openFolder"));
                    openFolderButton.setName("openFolderButton");
                    openFolderButton.setFocusable(false);
                }
//				{
//					saveButton = new JideButton();
//					toolBar.add(saveButton);
//					saveButton.setAction(getAction("save"));
//					saveButton.setName("saveButton");
//					saveButton.setFocusable(false);
//				}
                {
                    toolBar.addSeparator();
                }
                {
                    JButton button = new JideButton();
                    toolBar.add(button);
                    button.setAction(getAction("exitApp"));
                    button.setName("exitButton");
                    button.setFocusable(false);
                }
            }
            {
                JSeparator jSeparator = new JSeparator();
                toolBarPanel.add(jSeparator, BorderLayout.SOUTH);
            }
        }
        {
            treePanel = new JScrollPane();
            desktop = new JDesktopPane();
            JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, desktop);
            centerPane.setDividerLocation(150);
            topPanel.add(centerPane, BorderLayout.CENTER);
        }
        {
            statusBar = new StatusBar();
            topPanel.add(statusBar, BorderLayout.SOUTH);

            final LabelStatusBarItem label = new LabelStatusBarItem("Line");
            label.setText("Resource Manager for JavaXYQ.");
            statusBar.add(label, JideBoxLayout.FLEXIBLE);

            final MemoryStatusBarItem gc = new MemoryStatusBarItem();
            statusBar.add(gc, JideBoxLayout.FIX);

        }
        menuBar = new JMenuBar();
        {
            fileMenu = new JMenu();
            menuBar.add(fileMenu);
            fileMenu.setName("fileMenu");
//			{
//				JMenuItem menuItem = new JMenuItem();
//				fileMenu.add(menuItem);
//				menuItem.setAction(getAction("newFile"));
//			}
            {
                JMenuItem menuItem = new JMenuItem();
                fileMenu.add(menuItem);
                menuItem.setAction(getAction("open"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                fileMenu.add(menuItem);
                menuItem.setAction(getAction("openFolder"));
            }
//			{
//				JMenuItem menuItem = new JMenuItem();
//				fileMenu.add(menuItem);
//				menuItem.setAction(getAction("save"));
//			}
            fileMenu.addSeparator();
            {
                JMenuItem menuItem = new JMenuItem();
                fileMenu.add(menuItem);
                menuItem.setAction(getAction("exitApp"));
            }
        }
        {
            editMenu = new JMenu();
            menuBar.add(editMenu);
            editMenu.setName("editMenu");
            {
                JMenuItem menuItem = new JMenuItem();
                editMenu.add(menuItem);
                menuItem.setAction(getAction("copy"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                editMenu.add(menuItem);
                menuItem.setAction(getAction("cut"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                editMenu.add(menuItem);
                menuItem.setAction(getAction("paste"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                editMenu.add(menuItem);
                menuItem.setAction(getAction("delete"));
            }
        }
        {
            windowMenu = new JMenu("Window");
            windowMenu.setName("windowMenu");
            menuBar.add(windowMenu);
            windowMenu.addSeparator();
        }
        {
            helpMenu = new JMenu("Help");
            helpMenu.setName("helpMenu");
            menuBar.add(helpMenu);
            {
                JMenuItem menuItem = new JMenuItem();
                helpMenu.add(menuItem);
                menuItem.setAction(getAction("visitHome"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                helpMenu.add(menuItem);
                menuItem.setAction(getAction("showHelp"));
            }
            {
                JMenuItem menuItem = new JMenuItem();
                helpMenu.add(menuItem);
                menuItem.setAction(getAction("suggestion"));
            }
//			{
//				JMenuItem menuItem = new JMenuItem();
//				helpMenu.add(menuItem);
//				menuItem.setAction(getAction("donate"));
//			}
            {
                JMenuItem menuItem = new JMenuItem();
                helpMenu.add(menuItem);
                menuItem.setAction(getAction("showAbout"));
            }
        }

        JFrame mainFrame = getMainFrame();
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setContentPane(topPanel);
    }
}
