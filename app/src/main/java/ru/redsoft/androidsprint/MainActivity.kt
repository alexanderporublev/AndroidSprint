package ru.redsoft.androidsprint

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import ru.redsoft.androidsprint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setReorderingAllowed(true)
            .add<CategoriesListFragment>(R.id.fragment_container_view)
            .commit()

        binding.categoryButton.setOnClickListener{
            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, CategoriesListFragment::class.java, null, null)
            }
        }

        binding.favoriteButton.setOnClickListener{
            supportFragmentManager.commit {
                replace(R.id.fragment_container_view, FavoritesFragment::class.java, null, null)
            }
        }
    }
}