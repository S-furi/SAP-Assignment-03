package it.unibo.sap.ass02.persistence

import it.unibo.sap.ass02.domain.User
import it.unibo.sap.ass02.domain.UserImpl
import java.util.PriorityQueue

class UserRepositoryImpl : UserRepository {
    private val users: MutableMap<Int, PriorityQueue<UserCreditEvent>> = mutableMapOf()
    private val cachedUsers: MutableMap<Int, User> = mutableMapOf()

    override fun appendEvent(event: UserCreditEvent) {
        if (users[event.userId] != null) {
            users[event.userId]!!.add(event)
            if (cachedUsers[event.userId] != null) cachedUsers.remove(event.userId)
        } else {
            users[event.userId] = PriorityQueue<UserCreditEvent> { e1, e2 -> e1.timestamp.compareTo(e2.timestamp) }
        }
    }

    override fun collectUser(userId: Int): User? =
        cachedUsers[userId]
            ?: users[userId]?.fold(UserImpl(userId)) { acc, curr ->
                if (curr.creditUpdate > 0) {
                    acc.rechargeCredit(curr.creditUpdate)
                } else {
                    acc.decreaseCredit(curr.creditUpdate)
                }
                acc
            }?.also { cachedUsers[it.id] = it }
}
