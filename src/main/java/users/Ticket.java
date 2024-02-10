package users;

import Flights.Airline;
import Flights.Flight;
import Utils.Interfaces.HasId;

import java.io.Serializable;
import java.util.Optional;

public abstract class Ticket implements Serializable, HasId {
  private User owner = null;
  private final Flight flight;
  private final Double cost;
  private final String type;
  private Airline airline;

  public Ticket(Flight flight, Double cost, String type, Airline airline) {
    this.flight = flight;
    int costPercent = getCostPercent(airline);
    this.cost = cost + ((cost * costPercent) / 100);
    this.type = type;
    this.flight.addTicket(this);
    this.airline = airline;
  }

  public static int getCostPercent(Airline airline) {
    switch (airline) {
      case AZAL:
        return 5; // Відсоток становить 5% для авіакомпанії AZAL
      case AIR_SEUL:
        return 7; // Відсоток становить 7% для авіакомпанії AIR_SEUL
      case INTERJET:
        return 8; // Відсоток становить 8% для авіакомпанії INTERJET
      case JET_STAR:
        return 6; // Відсоток становить 6% для авіакомпанії JET_STAR
      case LACOMPAGINE:
        return 9; // Відсоток становить 9% для авіакомпанії LACOMPAGINE
      case DELTA:
        return 4; // Відсоток становить 4% для авіакомпанії DELTA
      case OMAN_AIR:
        return 7; // Відсоток становить 7% для авіакомпанії OMAN_AIR
      case MANGO:
        return 5; // Відсоток становить 5% для авіакомпанії MANGO
      case KOREAN_AIR:
        return 8; // Відсоток становить 8% для авіакомпанії KOREAN_AIR
      case TURKISH_AIRLINES:
        return 6; // Відсоток становить 6% для авіакомпанії TURKISH_AIRLINES
      case WESTJET:
        return 4; // Відсоток становить 4% для авіакомпанії WESTJET
      case XIANS_AIRLINES:
        return 7; // Відсоток становить 7% для авіакомпанії XIANS_AIRLINES
      case PEGASUS_AIRLINES:
        return 6; // Відсоток становить 6% для авіакомпанії PEGASUS_AIRLINES
      default:
        return 0; // Якщо відсоток для авіакомпанії не вказаний, повертаємо 0
    }
  }

  public Optional<User> getOwner() {
    return Optional.of(owner);
  }

  public Flight getFlight() {
    return this.flight;
  }


  public Double getCost() {
    return cost;
  }

  public String getType(){
   return this.type;
  }

  @Override
  public String toString() {
    return "Ticket{" +
        ", booking=" + (this.getOwner().isPresent() ? "true" : "false") +
        ", flight=" + flight.getId() +
        '}';
  }
}
