// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class FindMeetingQuery {
  private static final Collection<Event> NO_EVENTS = Collections.emptySet();
  private static final Collection<String> NO_ATTENDEES = Collections.emptySet();

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> ranges = new ArrayList<>();
    if((events == NO_EVENTS&& request.getDuration()<=TimeRange.WHOLE_DAY.duration()) 
      || request.getAttendees() == NO_ATTENDEES){
      ranges.add(TimeRange.WHOLE_DAY);
      return ranges;
    }else if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
      return ranges;
    }
    Collection<String> attendees = request.getAttendees();
    for(Event event: events){
      Collection<String> eAttendees = event.getAttendees();
      for(String attendee: attendees){
        if(eAttendees.contains(attendee)||eAttendees.isEmpty()){
          ranges.add(event.getWhen());
        }
      }
    }
    if(ranges.size()==0){
      ranges.add(TimeRange.WHOLE_DAY);
      return ranges;
    }
    Collection<TimeRange> overlapedRanges = new ArrayList<>();
    if(ranges.size()>1){
      ranges.sort(TimeRange.ORDER_BY_START);
      TimeRange r1 = ranges.get(0);
      for(int i = 1; i < ranges.size();i++){
        TimeRange r2 = ranges.get(i);
        if(r1.overlaps(r2)){
          if(r1.contains(r2)){
            overlapedRanges.add(TimeRange.fromStartEnd(r1.start(), r1.end(), false));
          }else{
            overlapedRanges.add(TimeRange.fromStartEnd(r1.start(), r2.end(), false));
          }
        }else{
          overlapedRanges.add(TimeRange.fromStartEnd(r1.start(), r1.end(), false));
          if(i+1 >= ranges.size()){
            overlapedRanges.add(TimeRange.fromStartEnd(r2.start(), r2.end(), false));
          }else{
            r1 = r2;
          }
        }
      }
    }else if(ranges.size()==1){
      overlapedRanges.add(TimeRange.fromStartEnd(ranges.get(0).start(), ranges.get(0).end(), false));
    }
    Collection<TimeRange> validRanges = getValidTimes(overlapedRanges,request.getDuration());
  return validRanges;
  }

  public Collection<TimeRange> getValidTimes(Collection<TimeRange> ranges, long duration){
    Collection<TimeRange> validRange = new ArrayList<>();
    if(ranges.size()==0){
      return validRange;
    }
    int start = TimeRange.START_OF_DAY;
    int end;
    for(TimeRange range: ranges){
      end = range.start();
      TimeRange curRange = TimeRange.fromStartEnd(start, end, false);
      if(curRange.duration() >= duration){
        validRange.add(curRange);
      }
      start = range.end();
    }
    TimeRange curRange = TimeRange.fromStartEnd(start,TimeRange.END_OF_DAY+1, false);
    if(curRange.duration() >= duration){
      validRange.add(curRange);
    }
    return validRange;
  }
}
