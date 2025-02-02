package rabbitescape.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import static rabbitescape.engine.i18n.Translation.*;
import static rabbitescape.ui.swing.SwingConfigSetup.*;

import rabbitescape.engine.CompletedLevelWinListener;
import rabbitescape.engine.LevelWinListener;
import rabbitescape.engine.MultiLevelWinListener;
import rabbitescape.engine.config.Config;
import rabbitescape.engine.config.ConfigTools;
import rabbitescape.engine.err.RabbitEscapeException;
import rabbitescape.engine.menu.AboutText;
import rabbitescape.engine.menu.ConfigBasedLevelsCompleted;
import rabbitescape.engine.menu.LevelMenuItem;
import rabbitescape.engine.menu.LevelsCompleted;
import rabbitescape.engine.menu.Menu;
import rabbitescape.engine.menu.MenuDefinition;
import rabbitescape.engine.menu.MenuItem;
import rabbitescape.engine.util.RealFileSystem;
import rabbitescape.render.BitmapCache;

public class MenuUi
{
    public static class UnknownMenuItemType extends RabbitEscapeException
    {
        private static final long serialVersionUID = 1L;

        public final String name;
        public final MenuItem.Type type;

        public UnknownMenuItemType( MenuItem item )
        {
            this.name = item.name;
            this.type = item.type;
        }
    }

    private class ButtonListener implements ActionListener
    {
        private final MenuItem item;

        public ButtonListener( MenuItem item )
        {
            this.item = item;
        }

        @Override
        public void actionPerformed( ActionEvent event )
        {
            switch ( item.type )
            {
                case MENU:
                {
                    SwingUtilities.invokeLater( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            stack.add( item.menu );
                            placeMenu();
                        }
                    } );
                    return;
                }
                case ABOUT:
                {
                    about();
                    return;
                }
                case LEVEL:
                {
                    level( (LevelMenuItem)item );
                    return;
                }
                case QUIT:
                {
                    frame.exit();
                    return;
                }
                case DEMO:
                {
                    return;
                }
                default:
                {
                    throw new UnknownMenuItemType( item );
                }
            }
        }
    }

    private static final Color backgroundColor = Color.WHITE;
    private static final Color buttonColor = Color.LIGHT_GRAY;

    private final RealFileSystem fs;
    private final PrintStream out;
    private final Locale locale;
    private final BitmapCache<SwingBitmap> bitmapCache;

    private final Stack<Menu> stack;
    private final Config uiConfig;
    private final MainJFrame frame;
    private final SwingSound sound;

    private final JPanel menuPanel;
    private final LevelsCompleted levelsCompleted;
    private SideMenu sidemenu;

    public MenuUi(
        RealFileSystem fs,
        PrintStream out,
        Locale locale,
        BitmapCache<SwingBitmap> bitmapCache,
        Config uiConfig,
        MainJFrame frame,
        SwingSound sound
    )
    {
        this.fs = fs;
        this.out = out;
        this.locale = locale;
        this.bitmapCache = bitmapCache;
        this.stack = new Stack<>();
        this.uiConfig = uiConfig;
        this.frame = frame;
        this.sound = sound;
        this.menuPanel = new JPanel( new GridBagLayout() );
        this.levelsCompleted = new ConfigBasedLevelsCompleted( uiConfig );

        stack.push(
            MenuDefinition.mainMenu(
                new ConfigBasedLevelsCompleted( uiConfig )
            )
        );

        init();
    }

    public void init()
    {
        Container contentPane = frame.getContentPane();

        contentPane.setLayout( new BorderLayout( 4, 4 ) );

        sidemenu = new SideMenu(
            contentPane,
            bitmapCache,
            new Dimension( 32, 32 ),
            uiConfig,
            backgroundColor
        );

        JScrollPane scrollPane = new JScrollPane( menuPanel  );
        contentPane.add( scrollPane, BorderLayout.CENTER );
        contentPane.setBackground( backgroundColor );
        scrollPane.setBackground( backgroundColor );
        scrollPane.getVerticalScrollBar().setUnitIncrement( 16 );
        menuPanel.setBackground( backgroundColor );

        placeMenu();

        frame.setBoundsFromConfig();

        frame.setTitle( t( "Rabbit Escape" ) );

        frame.pack();
        frame.setVisible( true );

        initListeners();
    }

    private void uninit()
    {
        frame.getContentPane().removeAll();
    }

    public void placeMenu()
    {
        Menu menu = stack.lastElement();

        menuPanel.removeAll();

        Dimension buttonSize = new Dimension( 200, 40 );

        JLabel label = new JLabel( t( menu.intro ) );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setForeground( Color.RED );
        label.setPreferredSize( buttonSize );
        menuPanel.add( label, constraints( 0 ) );

        int i = 1;
        for ( MenuItem item : menu.items )
        {
            JButton button = new JButton( t( item.name, item.nameParams ) );
            button.setBackground( buttonColor );
            button.addActionListener( new ButtonListener( item ) );
            button.setVisible( true );
            button.setEnabled( item.enabled );
            button.setPreferredSize( buttonSize );
            menuPanel.add( button, constraints( i ) );
            ++i;
        }

        sound.setMusic( "tryad-let_them_run" );

        frame.repaint();
        frame.revalidate();
    }

    public void refreshEnabledItems()
    {
        Menu menu = stack.lastElement();
        menu.refresh();
    }

    private GridBagConstraints constraints( int i )
    {
        return new GridBagConstraints(
            0,
            i,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.NONE,
            new Insets( 10, 10, 10, 10 ),
            0,
            0
        );
    }

    private void level( final LevelMenuItem item )
    {
        new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                uninit();

                new SwingSingleGameMain(
                    fs,
                    out,
                    locale,
                    bitmapCache,
                    uiConfig,
                    frame,
                    sound,
                    MenuUi.this
                ).launchGame(
                    new String[] { item.fileName },
                    winListeners( item )
                );

                return null;
            }
        }.execute();
    }

    protected LevelWinListener winListeners( LevelMenuItem item )
    {
        return new MultiLevelWinListener(
            new CompletedLevelWinListener(
                item.levelsDir, item.levelNumber, levelsCompleted ),
            new UpdateSwingMenuLevelWinListener( this )
        );
    }

    private void about()
    {
        JTextPane text = new JTextPane();
        text.setText( t( AboutText.text ) );
        text.setBackground( null );

        JOptionPane.showMessageDialog(
            frame,
            text,
            t( "About Rabbit Escape" ),
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void back()
    {
        stack.pop();

        if ( stack.empty() )
        {
            frame.exit();
        }
        else
        {
            SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        placeMenu();
                    }
                }
            );
        }
    }

    private void setMuted( boolean muted )
    {
        ConfigTools.setBool( uiConfig, CFG_MUTED, muted );
        uiConfig.save();
        sound.mute( muted );
    }

    private void initListeners()
    {
        sidemenu.mute.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent evt )
            {
                setMuted( sidemenu.mute.isSelected() );
            }
        } );

        MenuTools.clickOnKey( sidemenu.mute, "mute", KeyEvent.VK_M );

        sidemenu.back.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                back();
            }
        } );

        MenuTools.clickOnKey( sidemenu.back, "back", KeyEvent.VK_ESCAPE );

        sidemenu.exit.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent evt )
            {
                frame.exit();
            }
        } );

        MenuTools.clickOnKey( sidemenu.exit, "quit", KeyEvent.VK_Q );
    }
}
