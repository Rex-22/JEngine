package engine.components;

import engine.core.Camera;
import engine.gfx.Sprite;

import java.awt.*;

public class EntityRenderComponent extends Component {

    private Sprite m_Texture;

    public EntityRenderComponent(Sprite texture){
        m_Texture = texture;
    }

    @Override
    public void Init() {
        m_Texture.SetTransform(m_Parent.GetTransform());
    }

    @Override
    public void OnUpdate(float delta) {
        if (m_Parent.HasMoved()){
            m_Texture.SetTransform(m_Parent.GetTransform());
        }
    }

    @Override
    public void OnRender(Graphics g, Camera camera) {
        m_Texture.Render(g, camera);
    }

    public Sprite GetTexture() {
        return m_Texture;
    }

}