package librae

import ain.rp.MeshRenderer
import ain.window.Window

class UIRenderer(window: Window) : MeshRenderer<UIInstance>(UIRenderPipeline(window))