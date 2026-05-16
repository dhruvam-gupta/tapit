import sys
import os
import math

try:
    from PIL import Image, ImageDraw
except ImportError:
    import subprocess
    print("Installing Pillow...")
    subprocess.check_call([sys.executable, "-m", "pip", "install", "Pillow"])
    from PIL import Image, ImageDraw

def process_icons():
    img_path = "/Users/dhruvam/Downloads/Gemini_Generated_Image_imwcprimwcprimwc.png"
    if not os.path.exists(img_path):
        print("Image not found:", img_path)
        sys.exit(1)
        
    img = Image.open(img_path).convert("RGBA")
    
    # Get color
    r, g, b, a = img.getpixel((0, 0))
    bg_hex = f"#{r:02x}{g:02x}{b:02x}"
    
    res_dir = "/Users/dhruvam/.gemini/antigravity/scratch/TapIt/app/src/main/res"
    sizes = {"mdpi": 48,"hdpi": 72,"xhdpi": 96,"xxhdpi": 144,"xxxhdpi": 192}
    
    for density, size in sizes.items():
        folder = os.path.join(res_dir, f"mipmap-{density}")
        os.makedirs(folder, exist_ok=True)
        
        sq = img.resize((size, size), Image.Resampling.LANCZOS)
        sq.save(os.path.join(folder, "ic_launcher.png"))
        
        mask = Image.new('L', sq.size, 0)
        draw = ImageDraw.Draw(mask)
        draw.ellipse((0, 0, size, size), fill=255)
        
        rnd = Image.new('RGBA', sq.size, (0,0,0,0))
        rnd.paste(sq, (0,0), mask=mask)
        rnd.save(os.path.join(folder, "ic_launcher_round.png"))
        
    # Foreground
    os.makedirs(os.path.join(res_dir, "mipmap-xxxhdpi"), exist_ok=True)
    img.resize((432, 432), Image.Resampling.LANCZOS).save(os.path.join(res_dir, "mipmap-xxxhdpi", "ic_launcher_foreground.png"))
    
    # XML
    values_dir = os.path.join(res_dir, "values")
    os.makedirs(values_dir, exist_ok=True)
    with open(os.path.join(values_dir, "ic_launcher_color.xml"), "w") as f:
        f.write(f'<?xml version="1.0" encoding="utf-8"?>\n<resources>\n<color name="tapit_icon_bg">{bg_hex}</color>\n</resources>\n')
        
    v26_dir = os.path.join(res_dir, "mipmap-anydpi-v26")
    os.makedirs(v26_dir, exist_ok=True)
    xml = '<?xml version="1.0" encoding="utf-8"?>\n<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n<background android:color="@color/tapit_icon_bg"/>\n<foreground android:drawable="@mipmap/ic_launcher_foreground"/>\n</adaptive-icon>'
    with open(os.path.join(v26_dir, "ic_launcher.xml"), "w") as f:
        f.write(xml)
    with open(os.path.join(v26_dir, "ic_launcher_round.xml"), "w") as f:
        f.write(xml)
        
    print("Done")

if __name__ == "__main__":
    process_icons()
