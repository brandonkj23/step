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
import java.time.chrono.Era;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class FindMeetingQuery {

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    ArrayList<TimeRange> eRanges = new ArrayList<>();
    Collection<String> attendees = request.getAttendees();
    for(Event event: events){
      Collection<String> eAttendees = event.getAttendees();
      for(String attendee: attendees){
        if(eAttendees.contains(attendee)||eAttendees.isEmpty()){
          eRanges.add(event.getWhen());
        }
      }
    }
    List<TimeRange> overlapedRanges = new ArrayList<>();
      eRanges.sort(TimeRange.ORDER_BY_START);
      for(int i = 0; i < eRanges.size();i++){
        TimeRange currentUnion = eRanges.get(i);
        if(i+1 < eRanges.size()){          
          TimeRange next = eRanges.get(i+1);  
          if(currentUnion.overlaps(next)){
            if(!currentUnion.contains(next)){
            currentUnion = TimeRange.fromStartEnd(currentUnion.start(), next.end(), false);
            }
            i++;
          }
        }
        if(!(i+2 < eRanges.size()) || !currentUnion.overlaps(eRanges.get(i+2))){
          overlapedRanges.add(currentUnion);
        }
      }
    Collection<TimeRange> validRanges = getValidTimes(overlapedRanges,request.getDuration());
  return validRanges;
  }

  //for this funciton to work ranges must be a list of sorted TimeRanges with no overlapping intervals
  public Collection<TimeRange> getValidTimes(List<TimeRange> ranges, long duration){
    Collection<TimeRange> validRange = new ArrayList<>();
    if(duration > TimeRange.WHOLE_DAY.duration()){
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
