package dev.exceptionteam.sakura.features.command.argument

import dev.exceptionteam.sakura.features.command.argument.impl.ExecutorArgument

class ArgumentTree(
    private val root: Argument<*>
) {
    private val subs = mutableListOf<ArgumentTree>()

    fun appendArgument(argumentTree: ArgumentTree) {
        subs.add(argumentTree)
    }

    fun invoke(input: String) {
        val args = input.split(" ")
        findCorrectExecutorArgument(args.size, args)?.invoke(input)
    }

    fun complete(args: List<String>): List<String> {
        val index = args.size - 1
        val last = args.last()

        if (args.isEmpty()) {
            return emptyList()
        }

        return findArgumentsByIndex(index, args).mapNotNull { it.complete(last) }.flatten()
    }

    fun getArgumentTreeString(prefix: String = ""): String {
        val builder = StringBuilder()
        val nextPrefix = if (prefix.isBlank()) root.toString() else "$prefix $root"

        if (subs.isNotEmpty()) {
            subs.forEach {
                val subArgumentTreeString = it.getArgumentTreeString(nextPrefix)
                builder.appendLine(subArgumentTreeString)
            }
            return builder.toString()
        } else {
            return nextPrefix
        }
    }

    private fun findArgumentsByIndex(index: Int, args: List<String>): List<Argument<*>> {
        if (index == root.index) {
            return listOf(root)
        }

        if (!root.check(args[root.index])) {
            return emptyList()
        }

        return subs.flatMap { it.findArgumentsByIndex(index, args) }
    }

    private fun findCorrectExecutorArgument(index: Int, args: List<String>): ExecutorArgument? {
        return if (root.index != index) {
            if (root.check(args[root.index])) {
                subs.firstNotNullOfOrNull {
                    it.findCorrectExecutorArgument(index, args)
                }
            } else {
                null
            }
        } else if (root is ExecutorArgument) {
            root
        } else {
            null
        }
    }
}