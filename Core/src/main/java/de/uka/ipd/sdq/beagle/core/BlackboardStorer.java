package de.uka.ipd.sdq.beagle.core;

import java.io.Serializable;

/**
 * Interface for classes that wish to write their state to the Blackboard. The type of the
 * state object is defined by {@code WRITTEN_TYPE}.
 *
 * <p>To illustrate the usage of this interface, two examples follow. We look at a typical
 * use case: Two {@linkplain de.uka.ipd.sdq.beagle.core.analysis.ProposedExpressionAnalyser
 * ResultAnalysers}, {@code MyAnalyser} and {@code YourAnalyser}. Both want to store data
 * on the {@linkplain Blackboard} to keep track of whether there is something new to look
 * at.
 *
 * <p>{@code YourAnalyser} simply wants to keep track of the {@linkplain SeffLoop
 * SEFFLoops} he has already seen. He wants to use an {@code HashSet} to do so:
 *
 * <pre>
 *
 * <code>
 *
 * public class YourAnalyser implements ResultAnalyser, BlackboardStorer&lt;HashSet&lt;SeffLoop&gt;&gt; {
 *
 * 	public boolean canContribute(ReadOnlyBlackboardView blackboard) {
 * 		Set&lt;SeffLoop&gt; alreadySeen = blackboard.readFor(YourAnalyser.class);
 * 		Set&lt;SeffLoop&gt; allLoops = blackboard.getAllSEFFLoops();
 * 		return allLoops.size() &gt; 0 &amp;&amp; (alreadySeen == null || !alreadySeen.containsAll(allLoops));
 * 	}
 *
 * 	public boolean contribute(AnalyserBlackboardView blackboard) {
 * 		Set&lt;SeffLoop&gt; alreadySeen = blackboard.readFor(YourAnalyser.class);
 * 		if (alreadySeen == null) {
 * 			alreadySeen = new HashSet&lt;SeffLoop&gt;();
 * 			blackboard.writeFor(YourAnalyser.class, alreadySeen);
 * 		}
 * 		Set&lt;SeffLoop&gt; allLoops = blackboard.getAllSEFFLoops();
 * 		Set&lt;SeffLoop&gt; todo = allLoops.removeAll(alreadySeen);
 *
 * 		for (SeffLoop loop : todo) {
 * 			// do the logic here
 * 			alreadySeen.add(loop);
 * 		}
 * 	}
 * }
 *
 * </code>
 *
 * </pre>
 *
 * <p>{@code MyAnalyser} however, wants to use its own data structure:
 *
 * <pre>
 *
 * <code>
 * public MyAnalyserDataStructure implements Serializable {
 * 		// a whole bunch of getters and setters
 * }
 * </code>
 *
 * </pre>
 *
 * <pre>
 *
 * <code>
 * public class MyAnalyser implements ResultAnalyser, BlackboardStorer&lt;MyAnalyserDataStructure&gt; {
 *  // everything here is like above:
 *  public boolean canContribute(ReadOnlyBlackboardView blackboard) {
 *     MyAnalyserDataStructure stored = blackboard.readFor(MyAnalyser.class);
 *      // …
 *  }
 *
 *  public boolean contribute(AnalyserBlackboardView blackboard) {
 *      MyAnalyserDataStructure stored = blackboard.readFor(MyAnalyser.class);
 *      // …
 *      MyAnalyserDataStructure newData = new MyAnalyserDataStructure();
 *      // …
 *      blackboard.writeFor(MyAnalyser.class, newData);
 *      // …
 *  }
 * </code>
 *
 * </pre>
 *
 * @author Joshua Gleitze
 * @param <WRITTEN_TYPE> The type of data the BlackboardStorer wants to write to the
 *            blackboard.
 * @see Blackboard
 */
public interface BlackboardStorer<WRITTEN_TYPE extends Serializable> {
}
