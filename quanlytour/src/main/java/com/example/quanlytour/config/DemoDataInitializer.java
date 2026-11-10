package com.example.quanlytour.config;

import com.example.quanlytour.chat.Conversation;
import com.example.quanlytour.chat.ConversationRepository;
import com.example.quanlytour.chat.MessageService;
import com.example.quanlytour.dto.BookTicketRequest;
import com.example.quanlytour.dto.HoaDonResponse;
import com.example.quanlytour.dto.RegisterRequest;
import com.example.quanlytour.entity.LichTrinh;
import com.example.quanlytour.entity.Tour;
import com.example.quanlytour.repository.LichTrinhRepository;
import com.example.quanlytour.repository.TourRepository;
import com.example.quanlytour.repository.UserRepository;
import com.example.quanlytour.service.AuthService;
import com.example.quanlytour.service.HoaDonService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true")
@RequiredArgsConstructor
public class DemoDataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataInitializer.class);
    private static final String DEMO_PASSWORD = "Demo@12345";

    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final LichTrinhRepository lichTrinhRepository;
    private final ConversationRepository conversationRepository;
    private final AuthService authService;
    private final HoaDonService hoaDonService;
    private final MessageService messageService;

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername("tv1")) {
            return;
        }
        createMember("tv1", "Thanh vien 1", "0911111111");
        createMember("tv2", "Thanh vien 2", "0922222222");
        createMember("tv3", "Thanh vien 3", "0933333333");

        LichTrinh daLat = createLichTrinh(findOrCreateTour("Da Lat 3N2D", "Da Lat"), 30, 2500000.0);
        LichTrinh haLong = createLichTrinh(findOrCreateTour("Ha Long 2N1D", "Quang Ninh"), 45, 1800000.0);

        HoaDonResponse hoaDonTv1 = hoaDonService.bookTicket(new BookTicketRequest(daLat.getId(), 2), "tv1");
        HoaDonResponse hoaDonTv3 = hoaDonService.bookTicket(new BookTicketRequest(daLat.getId(), 1), "tv3");
        HoaDonResponse hoaDonTv2 = hoaDonService.bookTicket(new BookTicketRequest(haLong.getId(), 1), "tv2");

        Conversation doanDaLat = conversationRepository.findByLichTrinhId(daLat.getId()).orElseThrow();
        Conversation doanHaLong = conversationRepository.findByLichTrinhId(haLong.getId()).orElseThrow();
        messageService.sendMessage(doanDaLat.getId(), "tv1", "Chao ca doan, 6h sang tap trung o cong cong ty nhe");
        messageService.sendMessage(doanDaLat.getId(), "tv3", "Minh se den som 15 phut");
        messageService.sendMessage(doanDaLat.getId(), "tv1", "Nho mang CCCD de lam thu tuc");
        messageService.sendMessage(doanHaLong.getId(), "tv2", "Doan Ha Long co ai di chung xe tu Ha Noi khong?");

        LOGGER.info("Da tao du lieu demo: tv1, tv2, tv3 (mat khau {})", DEMO_PASSWORD);
        LOGGER.info("Hoa don: tv1 = {}, tv3 = {}, tv2 = {}", hoaDonTv1.id(), hoaDonTv3.id(), hoaDonTv2.id());
        LOGGER.info("Nhom doan: {} = {} (tv1, tv3); {} = {} (tv2)",
                doanDaLat.getName(), doanDaLat.getId(), doanHaLong.getName(), doanHaLong.getId());
    }

    private void createMember(String username, String customerName, String phone) {
        authService.register(new RegisterRequest(username, DEMO_PASSWORD, customerName, phone, username + "@example.com"));
    }

    private Tour findOrCreateTour(String tourName, String destination) {
        return tourRepository.findAll()
                .stream()
                .filter(tour -> tourName.equals(tour.getTourName()))
                .findFirst()
                .orElseGet(() -> {
                    Tour tour = new Tour();
                    tour.setTourName(tourName);
                    tour.setDepartureLocation("Ha Noi");
                    tour.setDestination(destination);
                    return tourRepository.save(tour);
                });
    }

    private LichTrinh createLichTrinh(Tour tour, int daysFromNow, Double basePrice) {
        LichTrinh lichTrinh = new LichTrinh();
        lichTrinh.setTour(tour);
        lichTrinh.setDepartureDate(LocalDate.now().plusDays(daysFromNow));
        lichTrinh.setBasePrice(basePrice);
        lichTrinh.setTotalSlots(20);
        lichTrinh.setBookedSlots(0);
        return lichTrinhRepository.save(lichTrinh);
    }

}
