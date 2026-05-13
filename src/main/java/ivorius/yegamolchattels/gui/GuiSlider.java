package ivorius.yegamolchattels.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GuiSlider extends GuiButton
{
    private final String label;
    private final List<GuiControlListener<GuiSlider>> listeners = new ArrayList<>();
    private float minValue = 0.0f;
    private float maxValue = 1.0f;
    private float value = 0.0f;
    private boolean dragging;

    public GuiSlider(int buttonId, int x, int y, int widthIn, int heightIn, String label)
    {
        super(buttonId, x, y, widthIn, heightIn, label);
        this.label = label;
        updateDisplayString();
    }

    public void addListener(GuiControlListener<GuiSlider> listener)
    {
        listeners.add(listener);
    }

    public void setMinValue(float minValue)
    {
        this.minValue = minValue;
        setValue(value);
    }

    public void setMaxValue(float maxValue)
    {
        this.maxValue = maxValue;
        setValue(value);
    }

    public void setValue(float value)
    {
        this.value = Math.max(minValue, Math.min(maxValue, value));
        updateDisplayString();
    }

    public float getValue()
    {
        return value;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean pressed = super.mousePressed(mc, mouseX, mouseY);
        if (pressed)
        {
            dragging = true;
            updateFromMouse(mouseX);
        }
        return pressed;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (visible && dragging)
            updateFromMouse(mouseX);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        dragging = false;
    }

    private void updateFromMouse(int mouseX)
    {
        float normalized = (mouseX - (x + 4)) / (float) (width - 8);
        normalized = Math.max(0.0f, Math.min(1.0f, normalized));
        value = minValue + (maxValue - minValue) * normalized;
        updateDisplayString();
        for (GuiControlListener<GuiSlider> listener : listeners)
            listener.valueChanged(this);
    }

    private void updateDisplayString()
    {
        displayString = String.format(Locale.ROOT, "%s: %.2f", label, value);
    }
}
