package app;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import fx.assetloaders.Assets;
import fx.display.ConfigWindow;
import fx.display.MainWindow;
import main.Updateable;

public class App implements Runnable {
    private boolean running = false;

    Graphics g;
    BufferStrategy bs;

    private ArrayList<Updateable> updateable = new ArrayList<>();

    private Assets assets;
    public MainWindow mWindow;
    public ConfigWindow config= new ConfigWindow();
    public static Keybinding keybinding = new Keybinding();

    private void start() {
        mWindow = new MainWindow("AI and Pathfinding", 540, 480, keybinding);
        assets = Assets.getInstance();
        updateable.add(keybinding);
        updateable.add(config);
        running = true;
    }

    private void tick() {
        if (!config.getIsOpen()) {
            mWindow.requestFocus();
        }
        for (Updateable u : updateable) {
            u.update();
        }
        if (keybinding.getKey("e") && !config.getIsOpen()) {
            config.openConfig(mWindow.focus);
        }
    }

    private void render() {
        if (mWindow.whiteboard.getBufferStrategy() == null) {
            mWindow.whiteboard.createBufferStrategy(2);
        } 
        bs = mWindow.whiteboard.getBufferStrategy();
        g = bs.getDrawGraphics();

        g.drawImage(assets.getTexture("player"), 0, 0, null);

        for (Updateable u : updateable) {
            u.update();
        }

        bs.show();
        g.dispose();
    }

    @Override
    public void run() {
        start();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                // updates++;
                delta--;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(frames);
                frames = 0;
                // updates = 0;
            }
        }
        stop();
    }

    private void stop() {
        
    }
}