package self.harmony.bashrandomh.util;

import rx.Observable;
import rx.subjects.BehaviorSubject;

//Created by selfharmony
public class ProgressSubjectSingleton {

    private ProgressSubjectSingleton() {
    }

    private static BehaviorSubject<Integer> mIntegerBehaviourSubject = BehaviorSubject.create(1);

    private static BehaviorSubject<Integer> getmIntegerBehaviourSubject() {
        return mIntegerBehaviourSubject;
    }

    public static Observable<Integer> getmIntegerObservable() {
        return mIntegerBehaviourSubject.asObservable();
    }

    public static void setValue(int value) {
        getmIntegerBehaviourSubject()
                .onNext(value);
    }
}
