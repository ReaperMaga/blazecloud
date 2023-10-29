package sh.maga.blazecloud.backend.exception;

import jakarta.validation.groups.Default;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface ValidationGroups {
    interface Post extends Default {
    }

    interface Put extends Default {
    }

    interface Delete extends Default {
    }

    interface Get extends Default {
    }

    interface Patch extends Default {
    }
}