package com.practice.composetutorial

import SampleData
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.composetutorial.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                Conversation(messages = SampleData.conversationSample)
            }
        }
    }

    @Composable
    fun Conversation(messages: List<Message>) {
        // 화면 상에 보이는 아이템만 구성하기 때문에
        // 긴 리스트를 뿌려주는데 효율적
        LazyColumn {
            items(messages) { message ->
                MessageCard(msg = message)
            }
        }
    }

    @Composable
    fun MessageCard(msg: Message) {
        // 메시지를 둘러싼 padding
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.profile_picture),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // 이미지 크기 40dp
                    .size(40.dp)
                    // 원 형태로 이미지 크롭
                    .clip(CircleShape)
                    // 테두리 굵기, 색상, 모양
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            // 이미지와 텍스트 사이 horizontal 방향으로 8dp 간격 주기
            Spacer(modifier = Modifier.width(8.dp))

            // 메시지가 확장되었는지 상태값 관리
            var isExpanded by remember { mutableStateOf(false) }

            // 확장하거나 줄일 때 색상 서서히 변경시킴
            val surfaceColor by animateColorAsState(
                if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                label = "",
            )

            // 클릭할 때마다 isExpanded 값 토글
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )

                // 작성자와 문장 사이 vertical 방향으로 4dp 간격 주기
                Spacer(modifier = Modifier.height(4.dp))

                // 문장을 둘러싼 사각형 형태의 컨테이너
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    color = surfaceColor,
                    // 1dp 단위로
                    modifier = Modifier.animateContentSize().padding(1.dp)
                ) {
                    Text(
                        text = msg.body,
                        modifier = Modifier.padding(all = 8.dp),
                        // 확장되었을 때 전체, 아니면 한 줄만 보여주기
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        // 글자가 더 있을 때 ... 로 보여주기
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // Preview 어노테이션 여러개 붙여도 됨
    // 아님 preview 함수 따로 만들든지
    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewMessageCard() {
        ComposeTutorialTheme {
            Surface {
                MessageCard(
                    msg = Message("Sumin", "I think, no I *know* I like you.")
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewConversation() {
        ComposeTutorialTheme {
            Conversation(messages = SampleData.conversationSample)
        }
    }
}
