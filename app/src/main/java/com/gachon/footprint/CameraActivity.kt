package com.gachon.footprint

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val fragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.CYAN))
            .thenAccept { material ->
                val marker = ShapeFactory.makeSphere(0.05f, Vector3.zero(), material)
                fragment.setOnTapArPlaneListener { hit, _, _ ->
                    val anchorNode = AnchorNode(hit.createAnchor())
                    anchorNode.setParent(fragment.arSceneView.scene)
                    TransformableNode(fragment.transformationSystem).apply {
                        setParent(anchorNode)
                        renderable = marker
                        select()
                    }
                }
            }.exceptionally {
                runOnUiThread { Toast.makeText(this, "AR 표식을 만들 수 없습니다", Toast.LENGTH_LONG).show() }
                null
            }
    }
}
