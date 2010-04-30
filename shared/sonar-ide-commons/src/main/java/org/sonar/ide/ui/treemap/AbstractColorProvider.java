/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.ui.treemap;

import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractColorProvider<MODEL> implements ColorProvider<MODEL> {
  private Color minColor;
  private Color maxColor;
  private float minValue;
  private float maxValue;

  protected AbstractColorProvider(float minValue, float maxValue) {
    this(new Color(255, 0, 50), new Color(0, 255, 50), minValue, maxValue);
  }

  protected AbstractColorProvider(Color minColor, Color maxColor, float minValue, float maxValue) {
    this.minColor = minColor;
    this.maxColor = maxColor;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  protected abstract float getColorValue(MODEL value);

  protected Color getColor(float value) {
    float[] maxcv = maxColor.getRGBColorComponents(null);
    float[] mincv = minColor.getRGBColorComponents(null);
    float[] comp = new float[3];
    float diff = maxValue - minValue;
    for (int i = 0; i < 3; i++) {
      comp[i] = (maxcv[i] - mincv[i]) / diff * (value - minValue) + mincv[i];
    }
    return new Color(comp[0], comp[1], comp[2]);
  }

  public Color getColor(MODEL value) {
    return getColor(getColorValue(value));
  }
}
