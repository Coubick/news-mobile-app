package com.example.news.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.news.R
import com.example.news.presentation.screens.news.NewsItem


@Composable
fun NewsCard(
    news: NewsItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))

    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .height(50.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Gray
                )
        ) {
            if (news.authorAvatarUrl != null) {
                AsyncImage(
                    model = news.authorAvatarUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp)
                        .clip(CircleShape)
                        .size(35.dp)

                )
            } else {
                Image(
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = "default avatar",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 4.dp)
                        .clip(CircleShape)
                        .size(35.dp),
                    contentScale = ContentScale.Crop,

                    )
            }

            Text(
                text = news.authorName,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.CenterVertically)
            )

            VerticalDivider(
                modifier = Modifier
                    .padding(start = 3.dp)
                    .height(17.dp)
                    .align(Alignment.CenterVertically),
                color = Color.DarkGray
            )

            Text(
                text = news.createdAt,
                modifier = Modifier
                    .padding(start = 3.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Row(

        ) {
            Text(
                text = news.sphereName,
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 8.dp)
            )

            Text(
                text = news.title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }

        if (news.imageUrl != null) {
            AsyncImage(
                model = news.imageUrl,
                contentDescription = news.title,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Image(
                painter = painterResource(R.drawable.news_title),
                contentDescription = "picture if url is null",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Text(
            text = news.description,
            modifier = Modifier
                .padding(start = 15.dp)
                .padding(top = 5.dp)
        )
    }
}

@Preview
@Composable
fun PreviewFun() {
    NewsCard(
        NewsItem(
            id = 1,
            title = "З а г о л о в о к",
            description = "description o chem-to",
            imageUrl = null,
            authorName = "valera",
            authorAvatarUrl = null,
            cityName = "SPB",
            sphereName = "music",
            createdAt = "24.04.2004"
        )
    )
}