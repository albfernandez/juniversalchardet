/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Universal charset detector code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 2001
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *          Shy Shalom <shooshX@gmail.com>
 *          Kohei TAKETA <k-tak@void.in> (Java port)
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.mozilla.universalchardet.prober;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.universalchardet.prober.sequence.HebrewModel;
import org.mozilla.universalchardet.prober.sequence.Ibm855Model;
import org.mozilla.universalchardet.prober.sequence.Ibm866Model;
import org.mozilla.universalchardet.prober.sequence.Koi8rModel;
import org.mozilla.universalchardet.prober.sequence.Latin5BulgarianModel;
import org.mozilla.universalchardet.prober.sequence.Latin5Model;
import org.mozilla.universalchardet.prober.sequence.Latin7Model;
import org.mozilla.universalchardet.prober.sequence.MacCyrillicModel;
import org.mozilla.universalchardet.prober.sequence.SequenceModel;
import org.mozilla.universalchardet.prober.sequence.ThaiModel;
import org.mozilla.universalchardet.prober.sequence.Win1251BulgarianModel;
import org.mozilla.universalchardet.prober.sequence.Win1251Model;
import org.mozilla.universalchardet.prober.sequence.Win1253Model;

public class SBCSGroupProber extends CharsetProber {
	
	private ProbingState state;
	private List<CharsetProber> probers = new ArrayList<>();
	private CharsetProber bestGuess;
	private int activeNum;
    
	public SBCSGroupProber() {
		super();

		probers.add(new SingleByteCharsetProber(new Win1251Model()));
		probers.add(new SingleByteCharsetProber(new Koi8rModel()));
		probers.add(new SingleByteCharsetProber(new Latin5Model()));
		probers.add(new SingleByteCharsetProber(new MacCyrillicModel()));
		probers.add(new SingleByteCharsetProber(new Ibm866Model()));
		probers.add(new SingleByteCharsetProber(new Ibm855Model()));
		probers.add(new SingleByteCharsetProber(new Latin7Model()));
		probers.add(new SingleByteCharsetProber(new Win1253Model()));
		probers.add(new SingleByteCharsetProber(new Latin5BulgarianModel()));
		probers.add(new SingleByteCharsetProber(new Win1251BulgarianModel()));
		probers.add(new SingleByteCharsetProber(new ThaiModel()));
        
        SequenceModel hebrewModel = new HebrewModel();
        HebrewProber hebprober = new HebrewProber();
        CharsetProber singleByte1 = new SingleByteCharsetProber(hebrewModel, false, hebprober);
        CharsetProber singleByte2 =new SingleByteCharsetProber(hebrewModel, true, hebprober);
        hebprober.setModalProbers(singleByte1, singleByte2);

		probers.add(hebprober);
		probers.add(singleByte1);
		probers.add(singleByte2);

		reset();
	}
    
	@Override
	public String getCharSetName() {
		if (this.bestGuess == null) {
			getConfidence();
			if (this.bestGuess == null) {
				this.bestGuess = this.probers.get(0);
			}
		}

		return this.bestGuess.getCharSetName();
	}

	@Override
	public float getConfidence() {
		float bestConf = 0.0f;
		float cf;

		if (this.state == ProbingState.FOUND_IT) {
			return 0.99f;
		} else if (this.state == ProbingState.NOT_ME) {
			return 0.01f;
		} else {
			for (CharsetProber prober : this.probers) {
				if (!prober.isActive()) {
					continue;
				}
				cf = prober.getConfidence();
				if (bestConf < cf) {
					bestConf = cf;
					this.bestGuess = prober;
				}
			}
		}

		return bestConf;
	}

	@Override
	public ProbingState getState() {
		return this.state;
	}

	@Override
	public ProbingState handleData(byte[] buf, int offset, int length) {
		ProbingState st;

		do {
			ByteBuffer newbuf = filterWithoutEnglishLetters(buf, offset, length);
			if (newbuf.position() == 0) {
				break;
			}
			for (CharsetProber prober : this.probers) {
				if (!prober.isActive()) {
					continue;
				}
				st = prober.handleData(newbuf.array(), 0, newbuf.position());
				if (st == ProbingState.FOUND_IT || 0.99f == prober.getConfidence()) {
					this.bestGuess = prober;
					this.state = ProbingState.FOUND_IT;
					break;
				} else if (st == ProbingState.NOT_ME) {
					prober.setActive(false);
					this.activeNum--;
					if (this.activeNum <= 0) {
						this.state = ProbingState.NOT_ME;
						break;
					}
				}
			}

		} while (false);

		return this.state;
	}

	@Override
	public final void reset() {
		this.activeNum = 0;
		for (CharsetProber prober : this.probers) {
			prober.reset();
			prober.setActive(true);
			this.activeNum++;
		}
		this.bestGuess = null;
		this.state = ProbingState.DETECTING;
	}

	@Override
	public void setOption() {
	}
}
