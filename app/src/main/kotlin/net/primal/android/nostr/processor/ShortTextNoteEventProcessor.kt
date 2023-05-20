package net.primal.android.nostr.processor

import net.primal.android.db.PrimalDatabase
import net.primal.android.feed.db.Post
import net.primal.android.nostr.model.NostrEvent
import net.primal.android.nostr.model.NostrEventKind

class ShortTextNoteEventProcessor(
    private val database: PrimalDatabase
) : NostrEventProcessor {

    override val kind = NostrEventKind.ShortTextNote

    override fun process(events: List<NostrEvent>) {
        database.posts().upsertAll(
            events = events.map { it.asPost() }
        )
    }

    private fun NostrEvent.asPost(): Post = Post(
        postId = this.id,
        authorId = this.pubKey,
        createdAt = this.createdAt,
        tags = this.tags,
        content = this.content,
        sig = this.sig,
    )
}