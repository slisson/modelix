package org.modelix.model.api

import org.modelix.model.area.IArea
import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleConcept : IConcept {
    override fun getShortName(): String {
        TODO("Not yet implemented")
    }

    override fun getLongName(): String {
        TODO("Not yet implemented")
    }

    override fun isSubconceptOf(superConcept: IConcept?): Boolean {
        return this == superConcept
    }

    override fun isExactly(concept: IConcept?): Boolean {
        return this == concept
    }

    override val properties: Iterable<IProperty>
        get() = TODO("Not yet implemented")
    override val childLinks: Iterable<IChildLink>
        get() = TODO("Not yet implemented")
    override val referenceLinks: Iterable<IReferenceLink>
        get() = TODO("Not yet implemented")

    override fun getProperty(name: String): IProperty {
        TODO("Not yet implemented")
    }

    override fun getChildLink(name: String): IChildLink {
        TODO("Not yet implemented")
    }

    override fun getReferenceLink(name: String): IReferenceLink {
        TODO("Not yet implemented")
    }
}

class SimpleNode(override val concept: IConcept? = null) : INode {
    override fun getArea(): IArea {
        TODO("Not yet implemented")
    }

    override val isValid: Boolean
        get() = TODO("Not yet implemented")
    override val reference: INodeReference
        get() = TODO("Not yet implemented")
    override var roleInParent: String? = null
    override var parent: INode? = null

    private val childrenByRole = HashMap<String?, MutableList<INode>>()

    override fun getChildren(role: String?): Iterable<INode> {
        return childrenByRole[role] ?: emptyList()
    }

    override val allChildren: Iterable<INode>
        get() = childrenByRole.values.flatten()

    override fun moveChild(role: String?, index: Int, node: INode) {
        val l = childrenByRole.getOrPut(role) { mutableListOf() }
        l.add(index, node)
        if (node is SimpleNode) {
            node.parent = this
            node.roleInParent = role
        }
        require(node.parent == this)
        require(node.roleInParent == role)
    }

    override fun addNewChild(role: String?, index: Int, concept: IConcept?): INode {
        TODO("Not yet implemented")
    }

    override fun removeChild(child: INode) {
        TODO("Not yet implemented")
    }

    override fun getReferenceTarget(role: String): INode? {
        TODO("Not yet implemented")
    }

    override fun setReferenceTarget(role: String, target: INode?) {
        TODO("Not yet implemented")
    }

    override fun getPropertyValue(role: String): String? {
        TODO("Not yet implemented")
    }

    override fun setPropertyValue(role: String, value: String?) {
        TODO("Not yet implemented")
    }
}

class NodeUtilTest {

    @Test
    fun getDescendantsIncludingItself() {
        val rootNode = SimpleNode()
        val child1 = SimpleNode()
        val child2 = SimpleNode()
        val grandChildA = SimpleNode()
        val grandChildB = SimpleNode()
        val grandChildC = SimpleNode()
        rootNode.moveChild("link_a", 0, child1)
        rootNode.moveChild("link_b", 0, child2)
        child1.moveChild("link_c", 0, grandChildA)
        child1.moveChild("link_c", 1, grandChildB)
        child1.moveChild("link_c", 2, grandChildC)
        assertEquals(hashSetOf(grandChildA), grandChildA.getDescendants(true).toSet())
        assertEquals(hashSetOf(grandChildB), grandChildB.getDescendants(true).toSet())
        assertEquals(hashSetOf(grandChildC), grandChildC.getDescendants(true).toSet())
        assertEquals(hashSetOf(child1, grandChildA, grandChildB, grandChildC), child1.getDescendants(true).toSet())
        assertEquals(hashSetOf(child2), child2.getDescendants(true).toSet())
        assertEquals(hashSetOf(rootNode, child1, child2, grandChildA, grandChildB, grandChildC), rootNode.getDescendants(true).toSet())
    }

    @Test
    fun getDescendantsNotIncludingItself() {
        val rootNode = SimpleNode()
        val child1 = SimpleNode()
        val child2 = SimpleNode()
        val grandChildA = SimpleNode()
        val grandChildB = SimpleNode()
        val grandChildC = SimpleNode()
        rootNode.moveChild("link_a", 0, child1)
        rootNode.moveChild("link_b", 0, child2)
        child1.moveChild("link_c", 0, grandChildA)
        child1.moveChild("link_c", 1, grandChildB)
        child1.moveChild("link_c", 2, grandChildC)
        assertEquals(hashSetOf(), grandChildA.getDescendants(false).toSet())
        assertEquals(hashSetOf(), grandChildB.getDescendants(false).toSet())
        assertEquals(hashSetOf(), grandChildC.getDescendants(false).toSet())
        assertEquals(hashSetOf(grandChildA, grandChildB, grandChildC), child1.getDescendants(false).toSet())
        assertEquals(hashSetOf(), child2.getDescendants(false).toSet())
        assertEquals(hashSetOf(child1, child2, grandChildA, grandChildB, grandChildC), rootNode.getDescendants(false).toSet())
    }

    @Test
    fun getAncestorIncludingItself() {
        val concept1 = SimpleConcept()
        val concept2 = SimpleConcept()
        val concept3 = SimpleConcept()
        val concept4 = SimpleConcept()
        val rootNode = SimpleNode(concept1)
        val child1 = SimpleNode(concept2)
        val grandChildA = SimpleNode(concept3)
        rootNode.moveChild("link_a", 0, child1)
        child1.moveChild("link_c", 0, grandChildA)
        assertEquals(null, null.getAncestor(null, true))
        assertEquals(null, null.getAncestor(concept1, true))
        assertEquals(null, null.getAncestor(concept2, true))
        assertEquals(null, null.getAncestor(concept3, true))
        assertEquals(null, null.getAncestor(concept4, true))
        assertEquals(null, rootNode.getAncestor(null, true))
        assertEquals(rootNode, rootNode.getAncestor(concept1, true))
        assertEquals(null, rootNode.getAncestor(concept2, true))
        assertEquals(null, rootNode.getAncestor(concept3, true))
        assertEquals(null, rootNode.getAncestor(concept4, true))
        assertEquals(null, child1.getAncestor(null, true))
        assertEquals(rootNode, child1.getAncestor(concept1, true))
        assertEquals(child1, child1.getAncestor(concept2, true))
        assertEquals(null, child1.getAncestor(concept3, true))
        assertEquals(null, child1.getAncestor(concept4, true))
        assertEquals(null, grandChildA.getAncestor(null, true))
        assertEquals(rootNode, grandChildA.getAncestor(concept1, true))
        assertEquals(child1, grandChildA.getAncestor(concept2, true))
        assertEquals(grandChildA, grandChildA.getAncestor(concept3, true))
        assertEquals(null, grandChildA.getAncestor(concept4, true))
    }

    @Test
    fun getAncestorNotIncludingItself() {
        val concept1 = SimpleConcept()
        val concept2 = SimpleConcept()
        val concept3 = SimpleConcept()
        val concept4 = SimpleConcept()
        val rootNode = SimpleNode(concept1)
        val child1 = SimpleNode(concept2)
        val grandChildA = SimpleNode(concept3)
        rootNode.moveChild("link_a", 0, child1)
        child1.moveChild("link_c", 0, grandChildA)
        assertEquals(null, null.getAncestor(null, false))
        assertEquals(null, null.getAncestor(concept1, false))
        assertEquals(null, null.getAncestor(concept2, false))
        assertEquals(null, null.getAncestor(concept3, false))
        assertEquals(null, null.getAncestor(concept4, false))
        assertEquals(null, rootNode.getAncestor(null, false))
        assertEquals(null, rootNode.getAncestor(concept1, false))
        assertEquals(null, rootNode.getAncestor(concept2, false))
        assertEquals(null, rootNode.getAncestor(concept3, false))
        assertEquals(null, rootNode.getAncestor(concept4, false))
        assertEquals(null, child1.getAncestor(null, false))
        assertEquals(rootNode, child1.getAncestor(concept1, false))
        assertEquals(null, child1.getAncestor(concept2, false))
        assertEquals(null, child1.getAncestor(concept3, false))
        assertEquals(null, child1.getAncestor(concept4, false))
        assertEquals(null, grandChildA.getAncestor(null, false))
        assertEquals(rootNode, grandChildA.getAncestor(concept1, false))
        assertEquals(child1, grandChildA.getAncestor(concept2, false))
        assertEquals(null, grandChildA.getAncestor(concept3, false))
        assertEquals(null, grandChildA.getAncestor(concept4, false))
    }
}
