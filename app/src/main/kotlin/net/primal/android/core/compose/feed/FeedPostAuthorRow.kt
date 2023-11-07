package net.primal.android.core.compose.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.primal.android.core.compose.AvatarThumbnailListItemImage
import net.primal.android.core.compose.NostrUserText
import net.primal.android.core.compose.asBeforeNowFormat
import net.primal.android.core.compose.media.model.MediaResourceUi
import net.primal.android.core.ext.findByUrl
import net.primal.android.theme.AppTheme
import net.primal.android.theme.PrimalTheme
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun FeedPostAuthorRow(
    modifier: Modifier = Modifier,
    authorDisplayName: String,
    postTimestamp: Instant? = null,
    authorAvatarUrl: String? = null,
    authorResources: List<MediaResourceUi> = emptyList(),
    authorInternetIdentifier: String? = null,
    onAuthorAvatarClick: () -> Unit,
) {
    val hasVerifiedBadge = !authorInternetIdentifier.isNullOrEmpty()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val resource = authorResources.findByUrl(url = authorAvatarUrl)
        val variant = resource?.variants?.minByOrNull { it.width }
        val imageSource = variant?.mediaUrl ?: authorAvatarUrl
        AvatarThumbnailListItemImage(
            source = imageSource,
            onClick = onAuthorAvatarClick,
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {

            val suffixText = if (postTimestamp != null) {
                val timestamp = postTimestamp.asBeforeNowFormat()

                buildAnnotatedString {
                    if (!hasVerifiedBadge) append(' ')
                    append(
                        AnnotatedString(
                            text = "| $timestamp",
                            spanStyle = SpanStyle(
                                color = AppTheme.extraColorScheme.onSurfaceVariantAlt2,
                                fontStyle = AppTheme.typography.bodySmall.fontStyle,
                            )
                        )
                    )
                }
            } else null

            NostrUserText(
                displayName = authorDisplayName,
                internetIdentifier = authorInternetIdentifier,
                annotatedStringSuffixBuilder = {
                    if (suffixText != null) {
                        append(suffixText)
                    }
                },
                style = AppTheme.typography.bodyMedium,
            )

            if (!authorInternetIdentifier.isNullOrEmpty()) {
                Text(
                    text = authorInternetIdentifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.extraColorScheme.onSurfaceVariantAlt2,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLightPostAuthorRow() {
    PrimalTheme(
        primalTheme = PrimalTheme.Sunrise,
    ) {
        Surface {
            FeedPostAuthorRow(
                authorDisplayName = "Donald Duck",
                postTimestamp = Instant.now().minusSeconds(3600.seconds.inWholeSeconds),
                authorAvatarUrl = null,
                authorResources = emptyList(),
                authorInternetIdentifier = "donald@the.duck",
                onAuthorAvatarClick = {},
            )
        }
    }
}

@Preview
@Composable
fun PreviewDarkPostAuthorRow() {
    PrimalTheme(
        primalTheme = PrimalTheme.Sunset,
    ) {
        Surface {
            FeedPostAuthorRow(
                authorDisplayName = "Donald Duck",
                postTimestamp = Instant.now().minusSeconds(3600.seconds.inWholeSeconds),
                authorAvatarUrl = null,
                authorResources = emptyList(),
                authorInternetIdentifier = "donald@the.duck",
                onAuthorAvatarClick = {},
            )
        }
    }
}
