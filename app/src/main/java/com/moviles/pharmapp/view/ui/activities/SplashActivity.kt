package com.moviles.pharmapp.view.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.item_medication.*

class SplashActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        ivLogoPharmapp.startAnimation(animation)

        val intent = Intent(this, RegisterActivity::class.java)
        animation.setAnimationListener(object : Animation.AnimationListener {
            /**
             *
             * Notifies the repetition of the animation.
             *
             * @param animation The animation which was repeated.
             */
            override fun onAnimationRepeat(animation: Animation?) {

            }

            /**
             *
             * Notifies the end of the animation. This callback is not invoked
             * for animations with repeat count set to INFINITE.
             *
             * @param animation The animation which reached its end.
             */
            override fun onAnimationEnd(animation: Animation?) {

                startActivity(intent)
                finish()
            }

            /**
             *
             * Notifies the start of the animation.
             *
             * @param animation The started animation.
             */
            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }
}