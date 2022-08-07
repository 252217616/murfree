package cn.murfree.context;

/**
 * LogContext
 *
 * @author lujun
 * @date 2022/8/5
 */
public class TestContext implements IContext {

    private int hp = 70;
    private int mp = 100;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }
}